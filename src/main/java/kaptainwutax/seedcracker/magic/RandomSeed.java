package kaptainwutax.seedcracker.magic;

import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;

public class RandomSeed {

	private static final LCG INVERSE_LCG = Rand.JAVA_LCG.combine(-1);

	/**
	 * Source: https://twitter.com/Geosquare_/status/1169623192153010176
	 * */
	public static boolean isRandomSeed(long worldSeed) {
		long upperBits = worldSeed >>> 32;
		long lowerBits = worldSeed & MagicMath.MASK_32;

		long a = (24667315 * upperBits + 18218081 * lowerBits + 67552711) >> 32;
		long b = (-4824621 * upperBits + 7847617 * lowerBits + 7847617) >> 32;
		long seed = INVERSE_LCG.nextSeed(7847617 * a - 18218081 * b);
		return new Rand(seed, false).nextLong() == worldSeed;
	}

}
