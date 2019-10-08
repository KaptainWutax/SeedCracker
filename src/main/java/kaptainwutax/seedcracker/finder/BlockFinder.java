package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.world.ChunkWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BlockFinder extends Finder {

    private Set<BlockState> targetBlockStates = new HashSet<>();

    public BlockFinder(World world, ChunkPos chunkPos, Block block) {
        super(world, chunkPos);
        this.targetBlockStates.addAll(block.getStateFactory().getStates());
    }

    public BlockFinder(World world, ChunkPos chunkPos, BlockState blockState) {
        super(world, chunkPos);
        this.targetBlockStates.add(blockState);
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();
        ChunkWrapper chunkWrapper = new ChunkWrapper(this.world, this.chunkPos);
        Chunk chunk = chunkWrapper.getChunk();

        for(BlockPos blockPos: CHUNK_POSITIONS) {
            BlockState currentState = chunk.getBlockState(blockPos);

            if(this.targetBlockStates.contains(currentState)) {
                result.add(this.chunkPos.getCenterBlockPos().add(blockPos));
            }
        }

        return result;
    }

}
