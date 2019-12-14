package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.util.Log;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecoratorCache {

    public static int INVALID = -1;

    private static DecoratorCache INSTANCE = new DecoratorCache();
    private boolean initialized = false;

    private Map<Biome, Map<Decorator<?>, Integer>> decoratorSaltMap = new HashMap<>();

    public static DecoratorCache get() {
        return INSTANCE;
    }

    public void initialize() {
        Registry.BIOME.forEach(biome -> {
            this.decoratorSaltMap.put(biome, new HashMap<>());

            for(GenerationStep.Feature genStep: GenerationStep.Feature.values()) {
                this.initializeBiomeStep(biome, genStep);
            }
        });

        this.initialized = true;
    }

    private void initializeBiomeStep(Biome biome, GenerationStep.Feature genStep) {
        List<ConfiguredFeature<?, ?>> features = biome.getFeaturesForStep(genStep);

        for(int i = 0; i < features.size(); i++) {
            FeatureConfig config = features.get(i).config;
            if(!(config instanceof DecoratedFeatureConfig))continue;
            this.decoratorSaltMap.get(biome).put(((DecoratedFeatureConfig)config).decorator.decorator, i + genStep.ordinal() * 10000);
        }
    }

    public int getSalt(Biome biome, Decorator<?> feature, boolean debug) {
        if(!this.initialized) {
            this.initialize();
        }

        Integer salt = this.decoratorSaltMap.get(biome).get(feature);

        if(salt == null) {
            if(debug) {
                Log.error(biome.getClass().getSimpleName() + " does not have decorator " + feature + ".");
            }

            salt = INVALID;
        }

        return salt;
    }

}
