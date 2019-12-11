package kaptainwutax.seedcracker.finder.population;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.population.DungeonData;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.util.PosIterator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DungeonFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        return false;
    });

    protected static Set<BlockPos> FLOOR_POSITION = PosIterator.create(
            new BlockPos(-3, -1, -3),
            new BlockPos(3, -1, 3)
    );

    public DungeonFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.SPAWNER);
        this.searchPositions = SEARCH_POSITIONS;
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

        Biome biome = this.world.getBiome(this.chunkPos.getCenterBlockPos().add(8, 8, 8));

        List<BlockPos> starts = result.stream()
                .map(pos -> new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15)).collect(Collectors.toList());

        List<List<Integer>> floorCallsList = result.stream()
                .map(pos -> this.getFloorCalls(this.getDungeonSize(pos), pos)).collect(Collectors.toList());

        result.forEach(pos -> {
            if(SeedCracker.get().onPopulationData(new DungeonData(this.chunkPos, biome, starts, floorCallsList))) {
                this.renderers.add(new Cube(pos, new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            }
        });

        return result;
    }

    public Vec3i getDungeonSize(BlockPos spawnerPos) {
        for(int xo = 4; xo >= 3; xo--) {
            for(int zo = 4; zo >= 3; zo--) {
                Block block = this.world.getBlockState(spawnerPos.add(xo, -1, zo)).getBlock();
                if(block != Blocks.MOSSY_COBBLESTONE)continue;
                if(block != Blocks.COBBLESTONE)continue;
                return new Vec3i(xo, 0, zo);
            }
        }

        return Vec3i.ZERO;
    }

    public List<Integer> getFloorCalls(Vec3i dungeonSize, BlockPos spawnerPos) {
        List<Integer> floorCalls = new ArrayList<>();

        for(int xo = -dungeonSize.getX(); xo <= dungeonSize.getX(); xo++) {
            for(int zo = -dungeonSize.getZ(); zo <= dungeonSize.getZ(); zo++) {
                Block block = this.world.getBlockState(spawnerPos.add(xo, -1, zo)).getBlock();

                if(block == Blocks.MOSSY_COBBLESTONE) {
                    floorCalls.add(DungeonData.MOSSY_COBBLESTONE_CALL);
                } else if(block == Blocks.COBBLESTONE) {
                    floorCalls.add(DungeonData.COBBLESTONE_CALL);
                }
            }
        }

        return floorCalls;
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
