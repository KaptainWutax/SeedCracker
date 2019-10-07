package kaptainwutax.seedcracker.finder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class Finder {

    protected static final List<BlockPos> CHUNK_POSITIONS = new ArrayList<>();
    protected static final List<BlockPos> SUB_CHUNK_POSITIONS = new ArrayList<>();

    static {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                for(int y = 0; y < 256; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if(y < 16)SUB_CHUNK_POSITIONS.add(pos);
                    CHUNK_POSITIONS.add(pos);
                }
            }
        }
    }

    public Finder() {

    }

    public abstract List<BlockPos> findInChunk(World world, ChunkPos chunkPos);

}
