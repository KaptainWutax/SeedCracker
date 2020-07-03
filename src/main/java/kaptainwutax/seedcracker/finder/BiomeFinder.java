package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
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

                if(SeedCracker.get().getDataStorage().addBiomeData(
                        new kaptainwutax.biomeutils.Biome.Data(
                                kaptainwutax.biomeutils.Biome.REGISTRY.get(Registry.BIOME.getRawId(biome)),
                                blockPos.getX(), blockPos.getZ()), DataStorage.POKE_BIOMES)) {
                    blockPos = this.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, blockPos).down();
                    result.add(blockPos);
                }
            }
        }

        result.forEach(pos -> {
            this.renderers.add(new Cube(pos, new Color(51, 204, 128)));
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new BiomeFinder(world, chunkPos));
        return finders;
    }

}
