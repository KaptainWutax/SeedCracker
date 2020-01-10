package kaptainwutax.seedcracker.util;

public class Seeds {

	public static long setRegionSeed(Rand rand, long worldSeed, int regionX, int regionZ, int salt) {
		long seed = (long)regionX * 341873128712L + (long)regionZ * 132897987541L + worldSeed + (long)salt;
		rand.setSeed(seed, true);
		return seed;
	}

	public static long setStructureStartSeed(Rand rand, long worldSeed, int chunkX, int chunkZ) {
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
		rand.setSeed(seed, true);
		rand.nextInt();
		return seed;
	}

	public static long setSlimeChunkSeed(Rand rand, long worldSeed, int chunkX, int chunkZ, long salt) {
		long seed = worldSeed + (long)(chunkX * chunkX * 4987142) + (long)(chunkX * 5947611) + (long)(chunkZ * chunkZ) * 4392871L + (long)(chunkZ * 389711) ^ salt;
		rand.setSeed(seed, true);
		return seed;
	}

}
