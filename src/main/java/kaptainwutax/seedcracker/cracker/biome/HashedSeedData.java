package kaptainwutax.seedcracker.cracker.biome;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.SeedData;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedutils.lcg.rand.JRand;
import net.minecraft.class_5217;

public class HashedSeedData extends SeedData {

	private final long hashedSeed;

	public HashedSeedData(long hashedSeed) {
		this.hashedSeed = hashedSeed;
	}

	@Override
	public boolean test(long seed, JRand rand) {
		return class_5217.method_27418(seed) == this.hashedSeed;
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
