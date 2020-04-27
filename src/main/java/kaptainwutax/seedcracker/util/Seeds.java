package kaptainwutax.seedcracker.util;

import kaptainwutax.seedcracker.cracker.biome.BiomeData;
import kaptainwutax.seedcracker.cracker.biome.source.EndBiomeSource;
import kaptainwutax.seedcracker.cracker.biome.source.NetherBiomeSource;
import kaptainwutax.seedcracker.cracker.biome.source.OverworldBiomeSource;
import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.cracker.structure.StructureFeatures;
import kaptainwutax.seedcracker.magic.PopulationReversal;
import kaptainwutax.seedcracker.magic.RandomSeed;
import kaptainwutax.seedcracker.util.math.LCG;
import net.minecraft.Bootstrap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.level.LevelGeneratorType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Seeds {

	public static long setRegionSeed(Rand rand, long worldSeed, int regionX, int regionZ, int salt) {
		long seed = (long)regionX * 341873128712L + (long)regionZ * 132897987541L + worldSeed + (long)salt;
		if(rand != null)rand.setSeed(seed, true);
		return seed;
	}

	public static long setPopulationSeed(Rand rand, long worldSeed, int posX, int posZ) {
		if(rand == null)rand = new Rand(0L);
		rand.setSeed(worldSeed, true);
		long a = rand.nextLong() | 1L;
		long b = rand.nextLong() | 1L;
		long seed = (long)posX * a + (long)posZ * b ^ worldSeed;
		rand.setSeed(seed, true);
		return seed;
	}

	public static long setStructureStartSeed(Rand rand, long worldSeed, int chunkX, int chunkZ) {
		if(rand == null)rand = new Rand(0L);
		rand.setSeed(worldSeed, true);
		long a = rand.nextLong();
		long b = rand.nextLong();
		long seed = (long)chunkX * a ^ (long)chunkZ * b ^ worldSeed;
		rand.setSeed(seed, true);
		return seed;
	}

	public static long setWeakSeed(Rand rand, long worldSeed, int chunkX, int chunkZ) {
		int sX = chunkX >> 4;
		int sZ = chunkZ >> 4;
		long seed = (long)(sX ^ sZ << 4) ^ worldSeed;

		if(rand != null) {
			rand.setSeed(seed, true);
			rand.nextInt();
		}

		return seed;
	}

	public static long setSlimeChunkSeed(Rand rand, long worldSeed, int chunkX, int chunkZ, long salt) {
		long seed = worldSeed + (long)(chunkX * chunkX * 4987142) + (long)(chunkX * 5947611) + (long)(chunkZ * chunkZ) * 4392871L + (long)(chunkZ * 389711) ^ salt;
		if(rand != null)rand.setSeed(seed, true);
		return seed;
	}

	public static void main(String[] args) {
		Bootstrap.initialize();

		String[] data = {
				"243893802777566 17854352 -10153344",
				"79580148162286 24665312 -22989152",
				"79580148162286 -11848752 363920",
				"7426225217742 -28524032 -7681312",
				"52460980589486 19238896 -16510672",
				"81226316041630 -22480304 3930464",
				"81226316041630 -13359280 29294688"
		};

		for(String datum: data) {
			String[] t = datum.split(Pattern.quote(" "));
			long seed = Long.parseLong(t[0]);
			int x = Integer.parseInt(t[1]);
			int z = Integer.parseInt(t[2]);
			int count = 0;

			for(long upperBits = 0; upperBits < (1L << 16) && count <= 5; upperBits++) {
				long worldSeed = (upperBits << 48) | seed;
				OverworldBiomeSource ow = new OverworldBiomeSource(worldSeed, LevelGeneratorType.DEFAULT);
				if(ow.sample(x - 18, 0, z - 18).getCategory() != Biome.Category.OCEAN)continue;

				if(ow.sample(x, 0, z) == Biomes.DESERT) {
					System.out.format("%d %d %d \n", worldSeed, x, z);
					count++;
				}
			}
		}
	}

	public static void main4(String[] args) {
		Bootstrap.initialize();
		long decoratorSeed = 34393509819256L;
		long populationSeed = (decoratorSeed ^ Rand.JAVA_LCG.multiplier) - 60007L;

		ChunkPos chunkPos = new ChunkPos(0, 1);

		for(int i = 0; i < 100; i++) {
			ChunkPos finalChunkPos = chunkPos;

			PopulationReversal.getWorldSeeds(populationSeed, chunkPos.x << 4, chunkPos.z << 4).forEach(structureSeed -> {
				long found = 0;

				for(long upperBits = 0; upperBits < (1L << 16) && found <= 5; upperBits++) {
					long worldSeed = (upperBits << 48) | structureSeed;
					OverworldBiomeSource ow = new OverworldBiomeSource(worldSeed, LevelGeneratorType.DEFAULT);
					if(ow.sample((finalChunkPos.x << 4) + 8, 0, (finalChunkPos.z << 4) + 8) != Biomes.DESERT)continue;
					if(ow.sample((finalChunkPos.x << 4) - 18, 0, (finalChunkPos.z << 4) - 18).getCategory() != Biome.Category.OCEAN)continue;
					System.out.println(worldSeed + ", " + finalChunkPos);
					found++;
				}
			});

			chunkPos = new ChunkPos(chunkPos.x + 1, chunkPos.z);
		}
	}

	public static ExecutorService SERVICE = Executors.newFixedThreadPool(8);

	public static void main6(String[] args) {
		int offsetX;
		int offsetZ;
		int posY;
		int[] pattern;
		long check;

		while(true) {
			Rand rand = new Rand(new Random().nextLong());
			offsetX = rand.nextInt(16);
			offsetZ = rand.nextInt(16);
			posY = rand.nextInt(256);

			//if(rand.getSeed() % (1L << 40) > 1L << 24) {
			//	continue;
			//}

			System.out.println(rand.getSeed());
			check = rand.getSeed();

			rand.nextInt(2);
			rand.nextInt(2);

			System.out.println(rand.getSeed());

			pattern = new int[81];
			for(int i = 0; i < pattern.length; i++) {
				pattern[i] = rand.nextInt(4) == 0 ? 0 : 1;
			}

			break;
		}

		System.out.println(Arrays.toString(pattern));

		long lower = (long)posY << 40;
		long upper = (long)(posY + 1) << 40;
		LCG back = Rand.JAVA_LCG.combine(-2);
		LCG skipFloorSize = Rand.JAVA_LCG.combine(2);

		for(int i = 0; i < 8; i++) {
			SERVICE.submit(() -> {
				for(long seed = lower; seed < upper; seed++) {
					long seedCopy = seed;
					long temp = back.nextSeed(seed);

					if(temp >>> (48 - 4) != offsetX) continue;
					temp = Rand.JAVA_LCG.nextSeed(temp);
					if(temp >>> (48 - 4) != offsetZ) continue;

					seedCopy = skipFloorSize.nextSeed(seedCopy);
					boolean floorMatches = true;

					for(int block : pattern) {
						seedCopy = Rand.JAVA_LCG.nextSeed(seedCopy);
						int nextInt4 = (int) (seedCopy >>> (48 - 2));

						if((block == 1 && nextInt4 == 0) || (block == 0 && nextInt4 != 0)) {
							floorMatches = false;
							break;
						}
					}

					if(floorMatches) {
						System.out.format("Found seed %d.\n", seed);
					}
				}
			});
		}
	}

	public static void main3(String[] args) {
		Bootstrap.initialize();

		Rand rand = new Rand(0L); //Cache the randomizer.

		for(long worldSeed = 0; worldSeed < 1L << 48; worldSeed++) {
			OverworldBiomeSource ow = new OverworldBiomeSource(worldSeed, LevelGeneratorType.DEFAULT);

			int progress = 0;

			for(int x = 0; x < 100; x++) {
				for(int z = 0; z < 100; z++) {
					BiomeData mushroom = new BiomeData(new BlockPos(x, 0, z), Biomes.MUSHROOM_FIELDS);
					BiomeData ice = new BiomeData(new BlockPos(x, 0, z), Biomes.ICE_SPIKES);
					BiomeData badlands = new BiomeData(new BlockPos(x, 0, z), Biomes.BADLANDS);
				}
			}

			System.out.format("Found seed %d.\n", worldSeed);
		}

		/*
		StructureData mansion = new StructureData(new ChunkPos(0, 0), StructureFeatures.WOODLAND_MANSION);

		Predicate<Biome> ml = biome -> biome.hasStructureFeature(Feature.WOODLAND_MANSION);
		Predicate<Biome> m = biome -> biome == Biomes.MUSHROOM_FIELDS || biome == Biomes.MUSHROOM_FIELD_SHORE;

		for(long structureSeed = 0; structureSeed < 1L << 48; structureSeed++) {
			ChunkPos mansionPos = StructureFeatures.WOODLAND_MANSION.getInRegion(rand, structureSeed, 0, 0);

			int x = (mansionPos.x << 4) + 9;
			int z = (mansionPos.z << 4) + 9;

			BiomeData roofedForest = new BiomeData(new BlockPos(x, 0, z), ml);
			BiomeData roofedForest1 = new BiomeData(new BlockPos(x + 34, 0, z + 34),ml);
			BiomeData roofedForest2 = new BiomeData(new BlockPos(x + 34, 0, z - 34), ml);
			BiomeData roofedForest3 = new BiomeData(new BlockPos(x - 34, 0, z - 34), ml);
			BiomeData roofedForest4 = new BiomeData(new BlockPos(x - 34, 0, z + 34), ml);
			BiomeData roofedForest5 = new BiomeData(new BlockPos(x, 0, z + 34), ml);
			BiomeData roofedForest6 = new BiomeData(new BlockPos(x, 0, z - 34), ml);
			BiomeData roofedForest7 = new BiomeData(new BlockPos(x - 34, 0, z), ml);
			BiomeData roofedForest8 = new BiomeData(new BlockPos(x + 34, 0, z), ml);

			BiomeData mushroom = new BiomeData(new BlockPos(x + 200, 0, z), m);
			BiomeData mushroom1 = new BiomeData(new BlockPos(x - 200, 0, z), m);
			BiomeData mushroom2 = new BiomeData(new BlockPos(x, 0, z + 200), m);
			BiomeData mushroom3 = new BiomeData(new BlockPos(x, 0, z - 200), m);

			for(long upperBits = 0; upperBits < 1L << 12; upperBits++) {
				long worldSeed = (upperBits << 48) | structureSeed;
				OverworldBiomeSource ow = new OverworldBiomeSource(worldSeed, LevelGeneratorType.DEFAULT);

				if(!roofedForest.test(ow)) continue;
				if(!roofedForest1.test(ow)) continue;
				if(!roofedForest2.test(ow)) continue;
				if(!roofedForest3.test(ow)) continue;
				if(!roofedForest4.test(ow)) continue;
				if(!roofedForest5.test(ow)) continue;
				if(!roofedForest6.test(ow)) continue;
				if(!roofedForest7.test(ow)) continue;
				if(!roofedForest8.test(ow)) continue;

				if(mushroom.test(ow) || mushroom1.test(ow) || mushroom2.test(ow) || mushroom3.test(ow)) {
					System.out.format("Found seed %d at %s.\n", worldSeed, mansionPos);}
				}
		}*/
	}

}
