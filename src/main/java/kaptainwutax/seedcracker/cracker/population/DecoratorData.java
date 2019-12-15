package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.cracker.DecoratorCache;
import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.Decorator;

public abstract class DecoratorData {

    private final ChunkPos chunkPos;
    private final Decorator<?> decorator;
    private final Biome biome;

    public DecoratorData(ChunkPos chunkPos, Decorator<?> decorator, Biome biome) {
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
        return this.testDecorator(new Rand(decoratorSeed, false));
    }

    public long getPopulationSeed(long structureSeed, int x, int z) {
        Rand rand = new Rand(structureSeed, true);
        long a = rand.nextLong() | 1L;
        long b = rand.nextLong() | 1L;
        return (long)x * a + (long)z * b ^ structureSeed;
    }

    public abstract boolean testDecorator(Rand rand);

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof DecoratorData) {
            DecoratorData decoratorData = ((DecoratorData)obj);
            return decoratorData.chunkPos.equals(this.chunkPos) && decoratorData.decorator == this.decorator;
        }

        return false;
    }

}
