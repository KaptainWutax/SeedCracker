package kaptainwutax.seedcracker.cracker.misc;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.SeedData;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.mc.seed.WorldSeed;

public class HashedSeedData extends SeedData {

	private final long hashedSeed;

	public HashedSeedData(long hashedSeed) {
		this.hashedSeed = hashedSeed;
	}

	@Override
	public boolean test(long seed, JRand rand) {
		return WorldSeed.toHash(seed) == this.hashedSeed;
	}

	public long getHashedSeed() {
		return this.hashedSeed;
	}

	@Override
	public double getBits() {
		return 64;
	}

	@Override
	public void onDataAdded(DataStorage dataStorage) {
		dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
	}

}
