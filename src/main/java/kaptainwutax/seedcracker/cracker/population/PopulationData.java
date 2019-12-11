package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.cracker.DecoratorCache;
import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PopulationData {

    private final ChunkPos chunkPos;
    private final Decorator<?> decorator;
    private final Biome biome;

    public PopulationData(ChunkPos chunkPos, Decorator<?> decorator, Biome biome) {
        this.chunkPos = chunkPos;
        this.decorator = decorator;
        this.biome = biome;
    }

    public final boolean test(long structureSeed) {
        long decoratorSeed = this.getPopulationSeed(structureSeed, this.chunkPos.x << 4, this.chunkPos.z << 4);
        int salt = DecoratorCache.get().getSalt(this.biome, this.decorator, true);

        if(salt == DecoratorCache.INVALID) {
            return false;
        }

        decoratorSeed += salt;
        decoratorSeed ^= Rand.JAVA_LCG.multiplier;
        decoratorSeed &= Rand.JAVA_LCG.modulo - 1;
        return this.testDecorator(decoratorSeed);
    }

    public long getPopulationSeed(long structureSeed, int x, int z) {
        Rand rand = new Rand(structureSeed, true);
        long a = rand.nextLong() | 1L;
        long b = rand.nextLong() | 1L;
        return (long)x * a + (long)z * b ^ structureSeed;
    }

    public abstract boolean testDecorator(long decoratorSeed);

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof PopulationData) {
            PopulationData populationData = ((PopulationData)obj);
            return populationData.chunkPos.equals(this.chunkPos) && populationData.decorator == this.decorator;
        }

        return false;
    }


    public abstract static class Feature {
        private Map<Biome, Long> CACHE = new HashMap<>();

        private GenerationStep.Feature genStep;
        private Decorator decorator;

        public Feature(GenerationStep.Feature genStep, Decorator decorator) {
            this.genStep = genStep;
            this.decorator = decorator;
        }

        public ChunkRandom buildRand(long worldSeed, Biome biome, ChunkPos chunkPos) {
            if(CACHE.containsKey(biome)) {
                return new ChunkRandom(CACHE.get(biome));
            }

            List<ConfiguredFeature<?>> features = biome.getFeaturesForStep(this.genStep);

            for(int i = 0; i < features.size(); i++) {
                ConfiguredFeature<?> feature = features.get(i);
                if(!(feature.config instanceof DecoratedFeatureConfig))continue;
                ConfiguredDecorator<?> currentDecorator = ((DecoratedFeatureConfig)feature.config).decorator;

                if(currentDecorator.decorator == this.decorator) {
                    BlockPos pos = new BlockPos(chunkPos.getStartX(), 0, chunkPos.getStartZ());
                    ChunkRandom chunkRandom = new ChunkRandom();
                    long populationSeed = chunkRandom.setSeed(worldSeed, pos.getX(), pos.getZ());
                    long seed = chunkRandom.setFeatureSeed(populationSeed, i, this.genStep.ordinal());
                    CACHE.put(biome, seed ^ Rand.JAVA_LCG.multiplier);
                    return chunkRandom;
                }
            }

            return null;
        }
    }

}
