package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.StructureData;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.render.Cuboid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MansionFinder extends Finder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        if(pos.getY() != 64)return true;
        if((pos.getX() & 15) != 0)return true;
        if((pos.getZ() & 15) != 0)return true;
        return false;
    });

    protected List<PieceFinder> finders = new ArrayList<>();
    protected Vec3i size = new Vec3i(16, 8, 16);

    public MansionFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);

        Direction.Type.HORIZONTAL.forEach(direction -> {
            PieceFinder finder = new PieceFinder(world, chunkPos, direction, size);

            finder.searchPositions = SEARCH_POSITIONS;

            buildStructure(finder);
            this.finders.add(finder);
        });
    }

    @Override
    public List<BlockPos> findInChunk() {
        Map<PieceFinder, List<BlockPos>> result = this.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {
            positions.removeIf(pos -> {
                //Figure this out, it's not a trivial task.
                return false;
            });

            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                if(SeedCracker.get().onStructureData(new StructureData(this.chunkPos, StructureData.WOODLAND_MANSION))) {
                    this.renderers.add(new Cuboid(pos, pieceFinder.getLayout(), new Vector4f(0.4f, 0.26f, 0.13f, 1.0f)));
                    this.renderers.add(new Cube(this.chunkPos.getCenterBlockPos().add(0, pos.getY(), 0), new Vector4f(0.4f, 0.26f, 0.13f, 1.0f)));
                }
            });
        });

        return combinedResult;
    }

    public Map<PieceFinder, List<BlockPos>> findInChunkPieces() {
        Map<PieceFinder, List<BlockPos>> result = new HashMap<>();

        this.finders.forEach(pieceFinder -> {
            result.put(pieceFinder, pieceFinder.findInChunk());
        });

        return result;
    }

    public void buildStructure(PieceFinder finder) {
        BlockState cobblestone = Blocks.COBBLESTONE.getDefaultState();
        BlockState birchPlanks = Blocks.BIRCH_PLANKS.getDefaultState();
        BlockState redCarpet = Blocks.RED_CARPET.getDefaultState();
        BlockState whiteCarpet = Blocks.WHITE_CARPET.getDefaultState();

        //TODO: Finish this.
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.OVERWORLD;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new MansionFinder(world, chunkPos));
        return finders;
    }

}

