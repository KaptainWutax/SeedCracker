package kaptainwutax.seedcracker.cracker;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;

import java.util.List;

public abstract class PopulationData {

    private final ChunkPos chunkPos;
    private final Feature feature;
    private final Biome biome;

    public PopulationData(ChunkPos chunkPos, Feature feature, Biome biome) {
        this.chunkPos = chunkPos;
        this.feature = feature;
        this.biome = biome;
    }

    public final boolean test(long worldSeed) {
        ChunkRandom chunkRandom = this.feature.buildRand(worldSeed, this.biome, this.chunkPos);
        return this.test(chunkRandom);
    }

    public abstract boolean test(ChunkRandom chunkRandom);

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof PopulationData) {
            PopulationData populationData = ((PopulationData)obj);
            return populationData.chunkPos.equals(this.chunkPos) && populationData.feature == this.feature;
        }

        return false;
    }


    public abstract static class Feature {
        private GenerationStep.Feature genStep;
        private Decorator decorator;

        public Feature(GenerationStep.Feature genStep, Decorator decorator) {
            this.genStep = genStep;
            this.decorator = decorator;
        }

        public ChunkRandom buildRand(long worldSeed, Biome biome, ChunkPos chunkPos) {
            List<ConfiguredFeature<?>> features = biome.getFeaturesForStep(this.genStep);

            for(int i = 0; i < features.size(); i++) {
                ConfiguredFeature<?> feature = features.get(i);
                if(!(feature.config instanceof DecoratedFeatureConfig))continue;
                ConfiguredDecorator<?> currentDecorator = ((DecoratedFeatureConfig)feature.config).decorator;

                if(currentDecorator.decorator == this.decorator) {
                    BlockPos pos = new BlockPos(chunkPos.getStartX(), 0, chunkPos.getStartZ());
                    ChunkRandom chunkRandom = new ChunkRandom();
                    long populationSeed = chunkRandom.setSeed(worldSeed, pos.getX(), pos.getZ());
                    chunkRandom.setFeatureSeed(populationSeed, i, this.genStep.ordinal());
                    return chunkRandom;
                }
            }

            return null;
        }
    }

}
