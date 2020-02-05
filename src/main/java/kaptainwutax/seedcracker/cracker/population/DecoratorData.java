package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.cracker.storage.SeedData;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.Seeds;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;

public abstract class DecoratorData extends SeedData {

    private final ChunkPos chunkPos;
    private final int salt;
    private final Biome biome;

    public DecoratorData(ChunkPos chunkPos, int salt, Biome biome) {
        this.chunkPos = chunkPos;
        this.salt = salt;
        this.biome = biome;
    }

    @Override
    public final boolean test(long seed, Rand rand) {
        long decoratorSeed = Seeds.setPopulationSeed(null, seed, this.chunkPos.x << 4, this.chunkPos.z << 4);

        decoratorSeed += salt;
        decoratorSeed ^= Rand.JAVA_LCG.multiplier;
        decoratorSeed &= Rand.JAVA_LCG.modulo - 1;
        rand.setSeed(decoratorSeed, false);
        return this.testDecorator(rand);
    }

    public abstract boolean testDecorator(Rand rand);

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof DecoratorData) {
            DecoratorData decoratorData = ((DecoratorData)obj);
            return decoratorData.chunkPos.equals(this.chunkPos) && decoratorData.salt == this.salt;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.chunkPos.hashCode() * 31 + this.salt;
    }

}
