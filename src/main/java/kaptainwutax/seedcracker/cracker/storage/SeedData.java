package kaptainwutax.seedcracker.cracker.storage;

import kaptainwutax.seedcracker.util.Rand;

public abstract class SeedData implements ISeedStorage {

	public abstract boolean test(long seed, Rand rand);

	public abstract double getBits();

}
