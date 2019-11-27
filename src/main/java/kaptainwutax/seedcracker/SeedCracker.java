package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.cracker.*;
import kaptainwutax.seedcracker.render.RenderQueue;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.level.LevelGeneratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SeedCracker implements ModInitializer {

	public static final Logger LOG = LogManager.getLogger("Seed Cracker");
    private static final SeedCracker INSTANCE = new SeedCracker();

    public List<Long> worldSeeds = null;
	public List<Long> structureSeeds = null;
	public List<Integer> pillarSeeds = null;

	private TimeMachine timeMachine = new TimeMachine();
	private List<StructureData> structureCache = new ArrayList<>();
	private List<PopulationData> populationCache = new ArrayList<>();
	private List<BiomeData> biomeCache = new ArrayList<>();

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public synchronized void clear() {
		this.worldSeeds = null;
		this.structureSeeds = null;
		this.pillarSeeds = null;
		this.structureCache.clear();
		this.biomeCache.clear();
	}

	public synchronized boolean onPillarData(PillarData pillarData) {
		if(pillarData != null && (this.pillarSeeds == null || this.pillarSeeds.isEmpty())) {
			LOG.warn("Looking for pillar seeds...");

			this.pillarSeeds = pillarData.getPillarSeeds();

			if(this.pillarSeeds.size() > 0) {
				LOG.warn("Finished search with " + this.pillarSeeds + (this.pillarSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				LOG.error("Finished search with no seeds.");
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

		if(this.structureSeeds == null && this.pillarSeeds != null && this.structureCache.size() >= 3) {
			this.structureSeeds = new ArrayList<>();
			LOG.warn("Looking for structure seeds with " + this.structureCache.size() + " structure features.");

			this.pillarSeeds.forEach(pillarSeed -> {
				timeMachine.buildStructureSeeds(pillarSeed, this.structureCache, this.structureSeeds);
			});

			if(this.structureSeeds.size() > 0) {
				LOG.warn("Finished search with " + this.structureSeeds.size() + (this.structureSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				LOG.error("Finished search with no seeds.");
			}

			this.structureCache.clear();
			this.onPopulationData(null);
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

	public synchronized boolean onPopulationData(PopulationData populationData) {
		boolean added = false;

		if(populationData != null && !this.populationCache.contains(populationData)) {
			this.populationCache.add(populationData);
			added = true;
		}

		if(this.worldSeeds == null && this.structureSeeds != null && this.populationCache.size() >= 4) {
			this.worldSeeds = new ArrayList<>();
			LOG.warn("Looking for world seeds with " + this.populationCache.size() + " decorators.");

			for(int i = 0; i < this.structureSeeds.size(); i++) {
				SeedCracker.LOG.warn("Progress " + (i * 100.0f) / this.structureSeeds.size() + "%...");

				long structureSeed = this.structureSeeds.get(i);

				for(long j = 0; j < (1L << 16); j++) {
					long worldSeed = (j << 48) | structureSeed;
					boolean goodSeed = true;

					for(PopulationData data: this.populationCache) {
						if(!data.test(worldSeed)) {
							goodSeed = false;
							break;
						}
					}

					if(goodSeed) {
						this.worldSeeds.add(worldSeed);
					}
				}
			}

			LOG.warn("Finished search with " + this.worldSeeds + (this.worldSeeds.size() == 1 ? " seed." : " seeds."));
		} else if(this.worldSeeds != null && populationData != null) {
			this.worldSeeds.removeIf(worldSeed -> !populationData.test(worldSeed));
		}

		return added;
	}

	public synchronized boolean onBiomeData(BiomeData biomeData) {
		boolean added = false;

		if(biomeData != null && !this.biomeCache.contains(biomeData)) {
			this.biomeCache.add(biomeData);
			added = true;
		}

		if(this.worldSeeds == null && this.structureSeeds != null && this.biomeCache.size() >= 6) {
			this.worldSeeds = new ArrayList<>();
			LOG.warn("Looking for world seeds with " + this.biomeCache.size() + " biomes.");

			for(int i = 0; i < this.structureSeeds.size(); i++) {
				SeedCracker.LOG.warn("Progress " + (i * 100.0f) / this.structureSeeds.size() + "%...");

				long structureSeed = this.structureSeeds.get(i);

				for (long j = 0; j < (1L << 16); j++) {
					long worldSeed = (j << 48) | structureSeed;
					boolean goodSeed = true;
					BiomeLayerSampler sampler = BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT,
							BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings())[1];

					for(BiomeData data : this.biomeCache) {
						if (!data.test(worldSeed, sampler)) {
							goodSeed = false;
							break;
						}
					}

					if(goodSeed) {
						this.worldSeeds.add(worldSeed);
					}
				}
			}

			if(this.worldSeeds.size() > 0) {
				LOG.warn("Finished search with " + this.worldSeeds + (this.worldSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				LOG.error("Finished search with no seeds.");
			}
		} else if(this.worldSeeds != null && biomeData != null) {
			this.worldSeeds.removeIf(worldSeed -> {
				BiomeLayerSampler sampler = BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT,
						BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings())[1];
				return !biomeData.test(worldSeed, sampler);
			});
		} else if(this.worldSeeds != null) {
			this.worldSeeds.removeIf(worldSeed -> {
				for(BiomeData data: this.biomeCache) {
					BiomeLayerSampler sampler = BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT,
							BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings())[1];
					if(!biomeData.test(worldSeed, sampler))return true;
				}

				return false;
			});
		}

		return added;
	}

	public static void main(String[] args) {
		Random rand = new Random(1234L);

		IntStream.range(0, 8).mapToObj((int_1x) -> {
			int int_2 = rand.nextInt(16);
			int int_3 = rand.nextInt(256);
			int int_4 = rand.nextInt(16);
			System.out.println("Created " + int_2 + ", " + int_3 + ", " + int_4);
			return new BlockPos(int_2, int_3, int_4);
		}).forEach(pos -> {
			System.out.println("Populating " + pos);
		});
	}

}
