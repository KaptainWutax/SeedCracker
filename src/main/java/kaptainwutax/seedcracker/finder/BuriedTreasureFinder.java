package kaptainwutax.seedcracker.finder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.List;

public class BuriedTreasureFinder extends BlockFinder {

    protected static final List<BlockState> CHEST_HOLDERS = new ArrayList<>();

    static {
        CHEST_HOLDERS.add(Blocks.SANDSTONE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.STONE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.ANDESITE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.GRANITE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.DIORITE.getDefaultState());
    }

    public BuriedTreasureFinder() {
        super(Blocks.CHEST);
    }

    @Override
    public List<BlockPos> findInChunk(World world, ChunkPos chunkPos) {
        //Gets all the positions with a chest in the chunk.
        List<BlockPos> result = super.findInChunk(world, chunkPos);

        result.removeIf(pos -> {
            //Buried treasure chests always generate at (9, 9) within a chunk.
            int localX = pos.getX() & 15;
            int localZ = pos.getZ() & 15;
            if(localX != 9 || localZ != 9)return true;

            //Only so many blocks can hold a treasure chest.
            BlockState chestHolder = world.getBlockState(pos.down());
            if(!CHEST_HOLDERS.contains(chestHolder))return true;

            //Check if the biome contains the buried treasure feature.
            Biome biome = world.getBiome(pos);
            if(!biome.hasStructureFeature(Feature.BURIED_TREASURE))return true;

            //Damn that chest be lucky!
            return true;
        });

        return result;
    }

}
