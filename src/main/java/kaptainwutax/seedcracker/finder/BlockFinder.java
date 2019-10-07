package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.world.ChunkWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockFinder extends Finder {

    private Set<BlockState> targetBlockStates = new HashSet<>();

    public BlockFinder(Block block) {
        this.targetBlockStates.addAll(block.getStateFactory().getStates());
    }

    public BlockFinder(BlockState blockState) {
        this.targetBlockStates.add(blockState);
    }

    @Override
    public List<BlockPos> findInChunk(World world, ChunkPos chunkPos) {
        List<BlockPos> result = new ArrayList<>();
        ChunkWrapper chunkWrapper = new ChunkWrapper(world, chunkPos);
        Chunk chunk = chunkWrapper.getChunk();

        for(BlockPos pos: CHUNK_POSITIONS) {
            BlockState currentState = chunk.getBlockState(pos);

            if(this.targetBlockStates.contains(currentState)) {
                result.add(chunkPos.getCenterBlockPos().add(pos));
            }
        }

        return result;
    }

}
