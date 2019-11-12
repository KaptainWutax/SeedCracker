package kaptainwutax.seedcracker.cracker;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.level.LevelGeneratorType;

public class BiomeData {

    private int x;
    private int z;
    private Biome biome;

    public BiomeData(int x, int z, Biome biome) {
        this.x = x;
        this.z = z;
        this.biome = biome;
    }

    public BiomeData(int x, int z, String biomeId) {
        this(x, z, Registry.BIOME.get(new Identifier(biomeId)));
    }

    public BiomeData(int x, int z, int biomeId) {
        this(x, z, Registry.BIOME.get(biomeId));
    }

    public boolean test(long worldSeed) {
        BiomeLayerSampler[] samplers = this.buildLayerSamplers(worldSeed);
        return samplers[0].sample(this.x, this.z) == this.biome;
    }

    private BiomeLayerSampler[] buildLayerSamplers(long worldSeed) {
        return BiomeLayers.build(worldSeed, LevelGeneratorType.DEFAULT,
                BiomeSourceType.VANILLA_LAYERED.getConfig().getGeneratorSettings()
        );
    }

}
