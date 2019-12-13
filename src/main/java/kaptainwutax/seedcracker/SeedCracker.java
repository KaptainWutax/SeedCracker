package kaptainwutax.seedcracker;

import io.netty.util.internal.ConcurrentSet;
import kaptainwutax.seedcracker.cracker.*;
import kaptainwutax.seedcracker.cracker.population.PopulationData;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedcracker.util.Rand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StrongholdFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeedCracker implements ModInitializer {

	public static final Logger LOG = LogManager.getLogger("Seed Cracker");
    private static final SeedCracker INSTANCE = new SeedCracker();

    public static final OverworldChunkGeneratorConfig CHUNK_GEN_CONFIG = new OverworldChunkGeneratorConfig();

    public List<Long> worldSeeds = null;
	public Set<Long> structureSeeds = null;
	public List<Integer> pillarSeeds = null;

	private TimeMachine timeMachine = new TimeMachine();
	private List<StructureData> structureCache = new ArrayList<>();
	private List<PopulationData> populationCache = new ArrayList<>();
	private List<BiomeData> biomeCache = new ArrayList<>();

		@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
		DecoratorCache.get().initialize();

		FakeBiomeSource fakeBiomeSource = new FakeBiomeSource(3698703115574076237L);
		System.out.println(VoronoiBiomeAccessType.INSTANCE.getBiome(3698703115574076237L, -176,0, -266, fakeBiomeSource));

		/*
		System.out.println("FETCHING SEEDS============");
		long structureSeed = 29131954246896L;
		ChunkPos chunkPos = new ChunkPos(117, 23);

		for(long j = 0; j < (1L << 16); j++) {
			long worldSeed = (j << 48) | structureSeed;

			if(initialize(worldSeed).contains(chunkPos)) {
				System.out.println(worldSeed);
				this.checkWorldSeed(worldSeed, chunkPos);
			}
		}
		System.out.println("FETCHING SEEDS============");
		*/

		/*
		System.out.println(9348141881871L ^ Rand.JAVA_LCG.multiplier);
		DungeonData data = new DungeonData(new ChunkPos(18, 8), Biomes.JUNGLE, new ArrayList<>(), new ArrayList<>());
		data.test(-2418316773073950375L);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("kaktoos14.txt"));

			for(int i = 0; i < (1 << 16); i++) {
				long worldSeed = 9368770777595L | ((long)i << 48);

				BiomeLayerSampler sampler = BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT,
						BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings())[1];

				if(sampler.sample(8, 8) == Biomes.DESERT) {
					writer.write(worldSeed + "\n");
				}
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	private void checkWorldSeed(long worldSeed, ChunkPos pos) {
		StrongholdFeature.Start start = new StrongholdFeature.Start(Feature.STRONGHOLD, pos.x, pos.z, BlockBox.empty(), 0, worldSeed);
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public void clear() {
		this.worldSeeds = null;
		this.structureSeeds = null;
		this.pillarSeeds = null;
		this.structureCache.clear();
		this.biomeCache.clear();
		this.populationCache.clear();
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

		if(this.structureSeeds == null && this.pillarSeeds != null && this.structureCache.size() + this.populationCache.size() >= 5) {
			this.structureSeeds = new ConcurrentSet<>();
			LOG.warn("Looking for structure seeds with " + this.structureCache.size() + " structure features.");
			LOG.warn("Looking for structure seeds with " + this.populationCache.size() + " population features.");

			this.pillarSeeds.forEach(pillarSeed -> {
				timeMachine.buildStructureSeeds(pillarSeed, this.structureCache, this.populationCache, this.structureSeeds);
			});

			if(this.structureSeeds.size() > 0) {
				LOG.warn("Finished search with " + this.structureSeeds + (this.structureSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				LOG.error("Finished search with no seeds.");
			}

			this.structureCache.clear();
			this.onPopulationData(null);
			this.onBiomeData(null);
		} else if(this.structureSeeds != null && structureData != null) {
			System.out.println(this.structureSeeds);
			System.out.println(this.biomeCache);
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

		this.onStructureData(null);
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

			this.structureSeeds.forEach(structureSeed -> {
				for (long j = 0; j < (1L << 16); j++) {
					long worldSeed = (j << 48) | structureSeed;
					boolean goodSeed = true;

					FakeBiomeSource fakeBiomeSource = new FakeBiomeSource(worldSeed);

					for(BiomeData data : this.biomeCache) {
						if (!data.test(worldSeed, fakeBiomeSource)) {
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
				LOG.warn("Finished search with " + this.worldSeeds + (this.worldSeeds.size() == 1 ? " seed." : " seeds."));
			} else {
				LOG.error("Finished search with no seeds.");
			}
		} else if(this.worldSeeds != null && biomeData != null) {
			this.worldSeeds.removeIf(worldSeed -> {
				FakeBiomeSource fakeBiomeSource = new FakeBiomeSource(worldSeed);
				return !biomeData.test(worldSeed, fakeBiomeSource);
			});
		} else if(this.worldSeeds != null) {
			this.worldSeeds.removeIf(worldSeed -> {
				for(BiomeData data: this.biomeCache) {
					FakeBiomeSource fakeBiomeSource = new FakeBiomeSource(worldSeed);
					if(!data.test(worldSeed, fakeBiomeSource))return true;
				}

				return false;
			});
		}

		return added;
	}

	public static void main(String[] args) throws Exception {
		for(int i = 0; i < 10000; i++) {
			Rand rand = new Rand(i, false);
			if(rand.nextInt(700) == 0)System.out.println(i);
		}

		/*Random rand = new Random(1234L);

		IntStream.range(0, 8).mapToObj((int_1x) -> {
			int int_2 = rand.nextInt(16);
			int int_3 = rand.nextInt(256);
			int int_4 = rand.nextInt(16);
			System.out.println("Created " + int_2 + ", " + int_3 + ", " + int_4);
			return new BlockPos(int_2, int_3, int_4);
		}).forEach(pos -> {
			System.out.println("Populating " + pos);
		});*/

		/*
		System.out.println(validSeed(65867021031296932L));

		BufferedReader reader = new BufferedReader(new FileReader("run/seeds.txt"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("run/kaktoos14.txt"));

		while(reader.ready()) {
			long seed = Long.parseLong(reader.readLine().split(Pattern.quote(" "))[0]);
			seed ^= Rand.JAVA_LCG.multiplier;
			seed -= 60007;

			writer.write(seed + "===========================================\n");

			for(int i = 0; i < (1 << 16); i++) {
				long worldSeed = seed | ((long)i << 48);

				BiomeLayerSampler sampler = BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT,
						BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings())[1];

				if(sampler.sample(8, 8) == Biomes.DESERT) {
					writer.write(worldSeed + "\n");
				}
			}

			writer.flush();
		}

		writer.close();*/
	}

	/*
	private static List<ChunkPos> initialize(long worldSeed) {
		BiomeLayerSampler sampler = BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT,
				BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings())[0];

		List<ChunkPos> startPositions = new ArrayList<>();
		List<Biome> validBiomes = Lists.newArrayList();
		Iterator biomeIterator = Registry.BIOME.iterator();

		while(biomeIterator.hasNext()) {
			Biome biome = (Biome)biomeIterator.next();
			if(biome != null && biome.hasStructureFeature(Feature.STRONGHOLD)) {
				validBiomes.add(biome);
			}
		}

		Random rand = new Random(worldSeed);
		double randomRadian = rand.nextDouble() * Math.PI * 2.0D;

		//Actually 128, but we don't care about all of them.
		for(int i = 0; i < 3; ++i) {
			double double_2 = 128.0D + (rand.nextDouble() - 0.5D) * 80.0D;
			int x = (int)Math.round(Math.cos(randomRadian) * double_2);
			int z = (int)Math.round(Math.sin(randomRadian) * double_2);

			BlockPos locatedPos = locateBiome(sampler, (x << 4) + 8, (z << 4) + 8, 112, validBiomes, rand);

			if(locatedPos != null) {
				x = locatedPos.getX() >> 4;
				z = locatedPos.getZ() >> 4;
			}

			startPositions.add(new ChunkPos(x, z));
			randomRadian += (Math.PI * 2.0D) / 3.0d;
		}

		return startPositions;
	}./

	//CHECK NEW 1.15 SAMPLER
	/*
	public static BlockPos locateBiome(BiomeLayerSampler sampler, int x, int z, int size, List<Biome> validBiomes, Random rand) {
		int int_4 = x - size >> 2;
		int int_5 = z - size >> 2;
		int int_6 = x + size >> 2;
		int int_7 = z + size >> 2;
		int int_8 = int_6 - int_4 + 1;
		int int_9 = int_7 - int_5 + 1;
		Biome[] biomeSample = sampler.sample(int_4, int_5, int_8, int_9);
		BlockPos pos = null;
		int int_10 = 0;

		for(int i = 0; i < int_8 * int_9; ++i) {
			int int_12 = int_4 + i % int_8	 << 2;
			int int_13 = int_5 + i / int_8 << 2;
			if (validBiomes.contains(biomeSample[i])) {
				if(pos == null || rand.nextInt(int_10 + 1) == 0) {
					pos = new BlockPos(int_12, 0, int_13);
				}

				++int_10;
			}
		}

		return pos;
	}*/

}
