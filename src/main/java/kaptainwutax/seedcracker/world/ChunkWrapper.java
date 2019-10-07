package kaptainwutax.seedcracker.world;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkWrapper {

    private final World world;
    private final ChunkPos chunkPos;
    private final long chunkHash;

    public ChunkWrapper(World world, ChunkPos chunkPos) {
        this.world = world;
        this.chunkPos = chunkPos;
        this.chunkHash = chunkPos.toLong();
    }

    public Chunk getChunk() {
        return this.world.getChunk(this.chunkPos.getCenterBlockPos());
    }

}


