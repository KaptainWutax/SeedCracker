package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.StructureData;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.List;

public class BuriedTreasureFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        //Buried treasure chests always generate at (9, 9) within a chunk.
        int localX = pos.getX() & 15;
        int localZ = pos.getZ() & 15;
        if(localX != 9 || localZ != 9)return true;

        return false;
    });

    protected static final List<BlockState> CHEST_HOLDERS = new ArrayList<>();

    static {
        CHEST_HOLDERS.add(Blocks.SANDSTONE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.STONE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.ANDESITE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.GRANITE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.DIORITE.getDefaultState());

        //Population can turn stone, andesite, granite and diorite into ores...
        CHEST_HOLDERS.add(Blocks.COAL_ORE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.IRON_ORE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.GOLD_ORE.getDefaultState());

        //Ocean can turn stone into gravel.
        CHEST_HOLDERS.add(Blocks.GRAVEL.getDefaultState());
    }

    public BuriedTreasureFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.CHEST);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        //Gets all the positions with a chest in the chunk.
        List<BlockPos> result = super.findInChunk();

        result.removeIf(pos -> {
            //Chest can't be waterlogged!
            BlockState chest = world.getBlockState(pos);
            if(chest.get(ChestBlock.WATERLOGGED))return true;

            //Only so many blocks can hold a treasure chest.
            BlockState chestHolder = world.getBlockState(pos.down());
            if(!CHEST_HOLDERS.contains(chestHolder))return true;

            //Check if the biome contains the buried treasure feature.
            Biome biome = world.getBiome(pos);
            if(!biome.hasStructureFeature(Feature.BURIED_TREASURE))return true;

            //Damn that chest be lucky!
            return false;
        });

        result.forEach(pos -> {
            if(SeedCracker.get().onStructureData(new StructureData(this.chunkPos, StructureData.BURIED_TREASURE))) {
                this.renderers.add(new Cube(pos, new Vector4f(1.0f, 1.0f, 0.0f, 1.0f)));
            }
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.OVERWORLD;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new BuriedTreasureFinder(world, chunkPos));
        return finders;
    }

}
