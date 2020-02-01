package kaptainwutax.seedcracker.cracker.storage;

import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.world.level.LevelProperties;

public class HashedSeedData extends SeedData {

	private final long hashedSeed;

	public HashedSeedData(long hashedSeed) {
		this.hashedSeed = hashedSeed;
	}

	@Override
	public boolean test(long seed, Rand rand) {
		return LevelProperties.sha256Hash(seed) == this.hashedSeed;
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
