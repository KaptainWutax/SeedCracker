package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.Seeds;
import net.minecraft.util.math.ChunkPos;

public class SlimeChunkData {

	protected ChunkPos chunkPos;
	protected boolean isSlimeChunk;

	public SlimeChunkData(ChunkPos chunkPos, boolean isSlimeChunk) {
		this.chunkPos = chunkPos;
		this.isSlimeChunk = isSlimeChunk;
	}

	public boolean test(long seed, Rand rand) {
		Seeds.setSlimeChunkSeed(rand, seed, this.chunkPos.x, this.chunkPos.z, 987234911L);
		return (rand.nextInt(10) == 0) == this.isSlimeChunk;
	}

}
