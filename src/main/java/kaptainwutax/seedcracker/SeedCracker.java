package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.render.RenderQueue;
import net.fabricmc.api.ModInitializer;

public class SeedCracker implements ModInitializer {

    private static final SeedCracker INSTANCE = new SeedCracker();
    private DataStorage dataStorage = new DataStorage();

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);

		/*
		long ss = 5718603440394L;
		LCG lcg = Rand.JAVA_LCG.combine(-5);

		for(int i = 0; i < 8; i++) {
			System.out.println("n");
			PopulationReversal.getWorldSeeds(ss, 31 * 16, 19 * 16).forEach(seed -> {
				System.out.println("Structure seed: " + seed);

				for(int u = 0; u < 1 << 16; u++) {
					long worldSeed = ((long)u << 48) | seed;

					if(RandomSeed.isRandomSeed(worldSeed)) {
						System.out.println("nextLong() equivalent: " + worldSeed);
					}
				}
			});

			ss = lcg.nextSeed(ss);
		}*/

		/*
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("all_mineral.txt")));

			StructureData t = new StructureData(new ChunkPos(0, 0), StructureFeatures.BURIED_TREASURE);
			StructureData s = new StructureData(new ChunkPos(0, 0), StructureFeatures.SHIPWRECK);
			StructureData v = new StructureData(new ChunkPos(1, 1), StructureFeatures.VILLAGE);

			BiomeData b1 = new BiomeData(new BlockPos(9, 0, 9), biome -> biome.hasStructureFeature(Feature.BURIED_TREASURE));
			BiomeData b2 = new BiomeData(new BlockPos(9, 0, 9), biome -> biome.hasStructureFeature(Feature.SHIPWRECK));
			BiomeData b3 = new BiomeData(new BlockPos(9 + 16, 0, 9 + 16), biome -> biome.hasStructureFeature(Feature.VILLAGE));

			SpawnPointData spawn = new SpawnPointData(BlockPos.ORIGIN, 50);

			ChestLootData loot = new ChestLootData(
					MCLootTables.BURIED_TREASURE_CHEST,
					new ChestLootData.Stack(Items.EMERALD, Predicates.MORE_OR_EQUAL_TO, 3),
					new ChestLootData.Stack(Items.IRON_INGOT, Predicates.MORE_OR_EQUAL_TO, 4),
					new ChestLootData.Stack(Items.GOLD_INGOT, Predicates.MORE_OR_EQUAL_TO, 3),
					new ChestLootData.Stack(Items.TNT, Predicates.MORE_OR_EQUAL_TO, 1),
					new ChestLootData.Stack(Items.DIAMOND, Predicates.MORE_OR_EQUAL_TO, 1)
			);

			int count = 0;
			Rand rand = new Rand(0L, false);

			for(long seed = (1L << 48) - 1; seed >= 0; seed--) {
				if(!t.test(seed, rand))continue;
				if(!s.test(seed, rand))continue;
				if(!v.test(seed, rand))continue;

				if(!loot.test(seed + 20002L, rand))continue;

				for(long u = 0L; u < 1L << 16; u++) {
					long worldSeed = (u << 48) | seed;
					FakeBiomeSource source = new FakeBiomeSource(worldSeed);
					if(!b1.test(source))continue;
					if(!b2.test(source))continue;
					if(!b3.test(source))continue;
					if(!spawn.test(source))continue;
					String s2 = "[" + (++count) + "] " + worldSeed + " with structure seed " + seed + "\n";
					System.out.print(s2);
					writer.write(s2);
					writer.flush();
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}*/

		/*
		long seed = 1_000_000_000_000_000L;
		int distance = 80;

		Predicate<Biome>[] trees = new Predicate[] {
				biome -> biome instanceof ForestBiome || biome instanceof WoodedHillsBiome, //OAK TREE
				biome -> biome instanceof SavannaBiome || biome instanceof SavannaPlateauBiome, //ACACIA TREE
				biome -> biome instanceof DarkForestHillsBiome || biome instanceof DarkForestBiome, //DARK OAK TREE
				biome -> biome instanceof SnowyTaigaBiome || biome instanceof TaigaBiome, //SPRUCE TREE
				biome -> biome instanceof JungleBiome || biome instanceof JungleHillsBiome || biome instanceof ModifiedJungleBiome, //JUNGLE TREE
		};

		Set<Integer> ids = new HashSet<>();

		do {
			FakeBiomeSource source = new FakeBiomeSource(seed);
			BlockPos spawn = SpawnPointData.getSpawnPoint(source);
			int x = spawn.getX();
			int z = spawn.getZ();

			//X direction
			for(int s = -1; s != 1; s = 1) {
				for(int d = 0; d < distance; d++) {
					Biome biome = BiomeData.sampleBiome(source, x + s * d, 0, z);

					for(int j = 0; j < trees.length; j++) {
						if(!ids.contains(j) && trees[j].test(biome)) {
							ids.add(j);
						}
					}
				}

				if(ids.size() >= trees.length - 1) {
					System.out.println(seed + ", " + ids);
					ids.clear();
					break;
				}

				ids.clear();

				//Z direction
				for(int d = 0; d < distance; d++) {
					Biome biome = BiomeData.sampleBiome(source, x, 0, z + s * d);

					for(int j = 0; j < trees.length; j++) {
						if(!ids.contains(j) && trees[j].test(biome)) {
							ids.add(j);
						}
					}
				}

				if(ids.size() >= trees.length - 1) {
					System.out.println(seed + ", " + ids);
					ids.clear();
					break;
				}

				ids.clear();
			}

			seed++;
		} while(seed != 0L);*/

		/*
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("season_7_seeds_negative.txt")));
			int count = 0;

			long seed = 0L;
			BiomeData m1 = new BiomeData(new BlockPos(-200, 0, 0), biome -> biome instanceof MushroomFieldsBiome || biome instanceof MushroomFieldShoreBiome);
			BiomeData m2 = new BiomeData(new BlockPos(200, 0, 0), biome -> biome instanceof MushroomFieldsBiome || biome instanceof MushroomFieldShoreBiome);
			BiomeData m3 = new BiomeData(new BlockPos(0, 0, -200), biome -> biome instanceof MushroomFieldsBiome || biome instanceof MushroomFieldShoreBiome);
			BiomeData m4 = new BiomeData(new BlockPos(0, 0, 200), biome -> biome instanceof MushroomFieldsBiome || biome instanceof MushroomFieldShoreBiome);

			do {
				FakeBiomeSource source = new FakeBiomeSource(seed);

				if(m1.test(source) && m2.test(source) && m3.test(source) && m4.test(source)) {
					String s = "[" + (++count) + "] " + seed + "\n";
					System.out.println(s);
					writer.write(s);
					writer.flush();
				}

				seed--;
			} while(seed != 0L);

			writer.close();
		} catch(Exception e) {
			;
		}*/

		/*
		long popSeed = 107038380818082L;

		for(int x = 0; x < 10000; x++) {
			StructureData b = new StructureData(new ChunkPos(x, 0), StructureFeatures.BURIED_TREASURE);
			BiomeData b1 = new BiomeData(new BlockPos(16 * x + 9, 0, 9), Biomes.BEACH);

			List<Long> worldSeeds = Magic.getSeedFromChunkseed(popSeed, x * 16, 0);

			int finalX = x;
			worldSeeds.forEach(structureSeed -> {
				if(b.test(structureSeed, new Rand(0L))) {
					System.out.println("structure seed " + structureSeed + " at x " + (finalX * 16));
					for(long u = 0L; u < 1L << 16; u++) {
						long worldSeed = (u << 48) | structureSeed;
						FakeBiomeSource source = new FakeBiomeSource(worldSeed);
						if(!b1.test(source))continue;
						System.out.println(worldSeed + " with structure seed " + structureSeed + " at x " + (finalX * 16));
					}
				}
			});
		}*/
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

    public DataStorage getDataStorage() {
		return this.dataStorage;
	}

}
