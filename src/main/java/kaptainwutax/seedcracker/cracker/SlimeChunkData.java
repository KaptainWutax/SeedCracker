package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.SeedData;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.Seeds;
import net.minecraft.util.math.ChunkPos;

public class SlimeChunkData extends SeedData {

	protected static final double BITS = Math.log(10) / Math.log(2);

	protected final ChunkPos chunkPos;
	protected final boolean isSlimeChunk;

	public SlimeChunkData(ChunkPos chunkPos, boolean isSlimeChunk) {
		this.chunkPos = chunkPos;
		this.isSlimeChunk = isSlimeChunk;
	}

	@Override
	public boolean test(long seed, Rand rand) {
		Seeds.setSlimeChunkSeed(rand, seed, this.chunkPos.x, this.chunkPos.z, 987234911L);
		return (rand.nextInt(10) == 0) == this.isSlimeChunk;
	}

	@Override
	public double getBits() {
		return BITS;
	}

	@Override
	public void onDataAdded(DataStorage dataStorage) {
		dataStorage.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
	}

}
