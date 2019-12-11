package kaptainwutax.seedcracker.finder.population;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DecoratorCache;
import kaptainwutax.seedcracker.cracker.population.EndGatewayData;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Cuboid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.ArrayList;
import java.util.List;

public class EndGatewayFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        return false;
    });

    public EndGatewayFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.END_GATEWAY);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        //If no end gateway is supposed to populate in this chunk, return.
        Biome biome = this.world.getBiome(this.chunkPos.getCenterBlockPos().add(8, 0, 8));

        if(DecoratorCache.get().getSalt(biome, Decorator.END_GATEWAY, false) == DecoratorCache.INVALID) {
            return new ArrayList<>();
        }

        List<BlockPos> result = super.findInChunk();
        List<BlockPos> newResult = new ArrayList<>();

        result.forEach(pos -> {
            int height = this.findHeight(pos);

            if(height >= 3 && height <= 9) {
                newResult.add(pos);

                if(SeedCracker.get().onPopulationData(new EndGatewayData(this.chunkPos, biome, pos, height))) {
                    this.renderers.add(new Cuboid(pos.add(-1, -2, -1), pos.add(2, 3, 2), new Vector4f(0.4f, 0.4f, 0.82f, 1.0f)));
                }
            }
        });

        return newResult;
    }

    private int findHeight(BlockPos pos) {
        int height = 0;

        while(pos.getY() >= 0) {
            pos = pos.down();
            height++;

            BlockState state = this.world.getBlockState(pos);

            //Bedrock generates below gateways.
            if(state.getBlock() == Blocks.BEDROCK || state.getBlock() != Blocks.END_STONE) {
                continue;
            }

            break;
        }

        return height - 1;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.THE_END;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EndGatewayFinder(world, chunkPos));
        return finders;
    }

}
