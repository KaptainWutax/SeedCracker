package kaptainwutax.seedcracker.cracker.storage;

import kaptainwutax.seedutils.lcg.rand.JRand;

public abstract class SeedData implements ISeedStorage {

	public abstract boolean test(long seed, JRand rand);

	public abstract double getBits();

}
