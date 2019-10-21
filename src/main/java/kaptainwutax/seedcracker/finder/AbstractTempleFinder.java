package kaptainwutax.seedcracker.finder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTempleFinder extends Finder {

    protected List<PieceFinder> finders = new ArrayList<>();
    protected final Vec3i size;

    protected Map<BlockPos, Vec3i> posToLayout = new HashMap<>();

    public AbstractTempleFinder(World world, ChunkPos chunkPos, Vec3i size) {
        super(world, chunkPos);

        Direction.Type.HORIZONTAL.forEach(direction -> {
            PieceFinder finder = new PieceFinder(world, chunkPos, direction, size, pos -> {
                if(pos.getX() != 0)return false;
                if(pos.getY() < 63)return false;
                if(pos.getZ() != 0)return false;
                return true;
            });

            buildStructure(finder);
            finders.add(finder);
        });

        this.size = size;
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        this.finders.forEach(finder -> {
            List<BlockPos> rawResult = finder.findInChunk();
            rawResult.forEach(raw -> posToLayout.put(raw, finder.getLayout()));
            result.addAll(rawResult);
        });

        return result;
    }

    public abstract void buildStructure(PieceFinder finder);

}
