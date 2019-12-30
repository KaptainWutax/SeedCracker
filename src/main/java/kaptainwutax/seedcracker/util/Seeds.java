package kaptainwutax.seedcracker.util;

public class Seeds {

	public static long setStructureSeed(Rand rand, long worldSeed, int regionX, int regionZ, int salt) {
		long seed = (long)regionX * 341873128712L + (long)regionZ * 132897987541L + worldSeed + (long)salt;
		rand.setSeed(seed, true);
		return seed;
	}

}
