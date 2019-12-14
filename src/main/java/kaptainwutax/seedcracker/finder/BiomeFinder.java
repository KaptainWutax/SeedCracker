package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.BiomeData;
import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class BiomeFinder extends Finder {

    public BiomeFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                BlockPos blockPos = this.chunkPos.getCenterBlockPos().add(x, 0, z);
                Biome biome = this.world.getBiome(blockPos);

                //TODO: Fix this multi-threading issue.
                if(biome == Biomes.THE_VOID) {
                    continue;
                }

                if(SeedCracker.get().onBiomeData(new BiomeData(blockPos.getX(), blockPos.getZ(), biome))) {
                    blockPos = this.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, blockPos).down();
                    result.add(blockPos);
                }
            }
        }

        result.forEach(pos -> {
            this.renderers.add(new Cube(pos, new Vector4f(0.2f, 0.8f, 0.5f, 1.0f)));
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.OVERWORLD;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new BiomeFinder(world, chunkPos));
        return finders;
    }

}
