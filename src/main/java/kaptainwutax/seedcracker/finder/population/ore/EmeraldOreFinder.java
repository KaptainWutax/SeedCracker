package kaptainwutax.seedcracker.finder.population.ore;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DecoratorCache;
import kaptainwutax.seedcracker.cracker.population.EmeraldOreData;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Cube;
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

public class EmeraldOreFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = Finder.buildSearchPositions(Finder.CHUNK_POSITIONS, pos -> {
        return false;
    });

    public EmeraldOreFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.EMERALD_ORE);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiome(this.chunkPos.getCenterBlockPos().add(8, 0, 8));

        if(DecoratorCache.get().getSalt(biome, Decorator.EMERALD_ORE, false) == DecoratorCache.INVALID) {
            return new ArrayList<>();
        }

        List<BlockPos> result = super.findInChunk();

        if(!result.isEmpty() && SeedCracker.get().onPopulationData(new EmeraldOreData(this.chunkPos, biome, result))) {
            result.forEach(pos -> {
                this.renderers.add(new Cube(pos, new Vector4f(0.0f, 1.0f, 0.0f, 1.0f)));
            });
        }

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.OVERWORLD;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EmeraldOreFinder(world, chunkPos));
        return finders;
    }

}
