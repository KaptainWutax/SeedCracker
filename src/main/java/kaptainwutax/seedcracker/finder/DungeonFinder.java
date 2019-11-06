package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.util.PosIterator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DungeonFinder extends BlockFinder {

    protected static Set<BlockPos> FLOOR_POSITION = PosIterator.create(
            new BlockPos(-3, -1, -3),
            new BlockPos(3, -1, 3)
    );

    public DungeonFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.SPAWNER);
    }

    @Override
    public List<BlockPos> findInChunk() {
        //Gets all the positions with a mob spawner in the chunk.
        List<BlockPos> result = super.findInChunk();

        result.removeIf(pos -> {
            BlockEntity blockEntity = this.world.getBlockEntity(pos);
            if(!(blockEntity instanceof MobSpawnerBlockEntity))return true;

            for(BlockPos blockPos: FLOOR_POSITION) {
                BlockPos currentPos = pos.add(blockPos);
                Block currentBlock = this.world.getBlockState(currentPos).getBlock();

                if(currentBlock == Blocks.COBBLESTONE) {}
                else if(currentBlock == Blocks.MOSSY_COBBLESTONE) {}
                else return true;
            }

            return false;
        });

        result.forEach(pos -> this.renderers.add(new Cube(pos, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f))));

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.OVERWORLD;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new DungeonFinder(world, chunkPos));

        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));

        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));

        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)));
        return finders;
    }

}
