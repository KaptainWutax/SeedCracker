package kaptainwutax.seedcracker.finder;

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

public abstract class AbstractTempleFinder extends Finder {

    protected List<PieceFinder> finders = new ArrayList<>();
    protected final Vec3i size;

    public AbstractTempleFinder(World world, ChunkPos chunkPos, Vec3i size) {
        super(world, chunkPos);

        Direction.Type.HORIZONTAL.forEach(direction -> {
            PieceFinder finder = new PieceFinder(world, chunkPos, direction, size);

            finder.searchPositions.removeIf(pos -> {
                if(pos.getX() != 0)return true;
                if(pos.getY() < 63)return true;
                if(pos.getZ() != 0)return true;
                return false;
            });

            buildStructure(finder);
            this.finders.add(finder);
        });

        this.size = size;
    }

    public List<BlockPos> findInChunkPiece(PieceFinder pieceFinder) {
        return pieceFinder.findInChunk();
    }

    public Map<PieceFinder, List<BlockPos>> findInChunkPieces() {
        Map<PieceFinder, List<BlockPos>> result = new HashMap<>();

        this.finders.forEach(pieceFinder -> {
            result.put(pieceFinder, this.findInChunkPiece(pieceFinder));
        });

        return result;
    }

    public abstract void buildStructure(PieceFinder finder);

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.OVERWORLD;
    }
}
