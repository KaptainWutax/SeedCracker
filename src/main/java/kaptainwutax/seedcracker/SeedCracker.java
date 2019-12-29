package kaptainwutax.seedcracker;

import io.netty.util.internal.ConcurrentSet;
import kaptainwutax.seedcracker.cracker.*;
import kaptainwutax.seedcracker.cracker.population.DecoratorData;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedcracker.util.Log;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.level.LevelProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeedCracker implements ModInitializer {

    private static final SeedCracker INSTANCE = new SeedCracker();

    public long hashedWorldSeed = -1;

    public List<Long> worldSeeds = null;
	public Set<Long> structureSeeds = null;
	public List<Integer> pillarSeeds = null;

	private TimeMachine timeMachine = new TimeMachine();
	private List<StructureData> structureCache = new ArrayList<>();
	private List<DecoratorData> decoratorCache = new ArrayList<>();
	private List<BiomeData> biomeCache = new ArrayList<>();

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
		DecoratorCache.get().initialize();
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public void clear() {
        this.hashedWorldSeed = -1;
		this.worldSeeds = null;
		this.structureSeeds = null;
		this.pillarSeeds = null;
		this.structureCache.clear();
		this.biomeCache.clear();
		this.decoratorCache.clear();
	}

	public synchronized boolean onPillarData(PillarData pillarData) {
		if(pillarData != null && (this.pillarSeeds == null || this.pillarSeeds.isEmpty())) {
			Log.warn("Looking for pillar seeds...");

			this.pillarSeeds = pillarData.getPillarSeeds();

			if(this.pillarSeeds.size() > 0) {
				Log.warn("Finished search with " + this.pillarSeeds + (this.pillarSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				Log.error("Finished search with no seeds.");
			}

			this.onStructureData(null);
			return true;
		}

        return false;
    }

	public synchronized boolean onStructureData(StructureData structureData) {
		boolean added = false;

		if(structureData != null && !this.structureCache.contains(structureData)) {
			this.structureCache.add(structureData);
			added = true;
		}

		if(this.structureSeeds == null && this.pillarSeeds != null && this.structureCache.size() + this.decoratorCache.size() >= 5) {
			this.structureSeeds = new ConcurrentSet<>();
			Log.warn("Looking for structure seeds with " + this.structureCache.size() + " structure features.");
			Log.warn("Looking for structure seeds with " + this.decoratorCache.size() + " decorator features.");

			this.pillarSeeds.forEach(pillarSeed -> {
				timeMachine.buildStructureSeeds(pillarSeed, this.structureCache, this.decoratorCache, this.structureSeeds);
			});

			if(this.structureSeeds.size() > 0) {
				Log.warn("Finished search with " + this.structureSeeds + (this.structureSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				Log.error("Finished search with no seeds.");
			}

			this.structureCache.clear();
			this.onDecoratorData(null);
			this.onBiomeData(null);
		} else if(this.structureSeeds != null && structureData != null) {
			this.structureSeeds.removeIf(structureSeed -> {
				ChunkRandom chunkRandom = new ChunkRandom();
				chunkRandom.setStructureSeed(structureSeed, structureData.getRegionX(), structureData.getRegionZ(), structureData.getSalt());
				return !structureData.test(chunkRandom);
			});

			this.onBiomeData(null);
		}

		return added;
	}

	public synchronized boolean onDecoratorData(DecoratorData decoratorData) {
		boolean added = false;

		if(decoratorData != null && !this.decoratorCache.contains(decoratorData)) {
			this.decoratorCache.add(decoratorData);
			added = true;
		}

		this.onStructureData(null);
		return added;
	}

	public synchronized boolean onBiomeData(BiomeData biomeData) {
		boolean added = false;

		if(biomeData != null && !this.biomeCache.contains(biomeData)) {
			this.biomeCache.add(biomeData);
			added = true;
		}

		if(this.worldSeeds == null && this.structureSeeds != null && this.biomeCache.size() >= 5) {
			this.worldSeeds = new ArrayList<>();

			if(this.hashedWorldSeed != -1) {
				Log.warn("SHA2 seed was found.");
				Log.warn("Looking for world seeds with hash [" + this.hashedWorldSeed + "].");

				for(long structureSeed: this.structureSeeds) {
					for(long j = 0; j < (1L << 16); j++) {
						long worldSeed = (j << 48) | structureSeed;
						long hash = LevelProperties.sha256Hash(worldSeed);

						if(hash == this.hashedWorldSeed) {
							this.worldSeeds.add(worldSeed);
							Log.warn("Finished search with " + this.worldSeeds + (this.worldSeeds.size() == 1 ? " seed." : " seeds."));
							return added;
						}
					}
				}

				Log.error("Finished search with no seeds, reverting to biomes.");
			}

			Log.warn("Looking for world seeds with " + this.biomeCache.size() + " biomes.");

			this.structureSeeds.forEach(structureSeed -> {
				for(long j = 0; j < (1L << 16); j++) {
					long worldSeed = (j << 48) | structureSeed;
					boolean goodSeed = true;

					FakeBiomeSource fakeBiomeSource = new FakeBiomeSource(worldSeed);

					for(BiomeData data : this.biomeCache) {
						if(!data.test(fakeBiomeSource)) {
							goodSeed = false;
							break;
						}
					}

					if(goodSeed) {
						this.worldSeeds.add(worldSeed);
					}
				}
			});

			if(this.worldSeeds.size() > 0) {
				Log.warn("Finished search with " + this.worldSeeds + (this.worldSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				Log.error("Finished search with no seeds.");
			}
		} else if(this.worldSeeds != null && biomeData != null) {
			this.worldSeeds.removeIf(worldSeed -> {
				FakeBiomeSource fakeBiomeSource = new FakeBiomeSource(worldSeed);
				return !biomeData.test(fakeBiomeSource);
			});
		} else if(this.worldSeeds != null) {
			this.worldSeeds.removeIf(worldSeed -> {
				for(BiomeData data: this.biomeCache) {
					FakeBiomeSource fakeBiomeSource = new FakeBiomeSource(worldSeed);
					if(!data.test(fakeBiomeSource))return true;
				}

				return false;
			});
		}

		return added;
	}

}
