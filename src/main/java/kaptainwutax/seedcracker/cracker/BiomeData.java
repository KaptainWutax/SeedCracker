package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.ISeedStorage;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;

import java.util.function.Predicate;

public class BiomeData implements ISeedStorage {

    private BlockPos pos;

    private Biome biome;
    private Predicate<Biome> biomePredicate;

    public BiomeData(BlockPos pos, Biome biome) {
        this.pos = pos;
        this.biome = biome;
    }

    public BiomeData(BlockPos pos, int biomeId) {
        this.pos = pos;
        this.biome = Registry.BIOME.get(biomeId);
    }


    public BiomeData(BlockPos pos, Predicate<Biome> biomePredicate) {
        this.pos = pos;
        this.biomePredicate = biomePredicate;
    }

    public boolean test(FakeBiomeSource source) {
        if(this.biome == null) {
            return this.biomePredicate.test(sampleBiome(source, this.pos.getX(), this.pos.getY(), this.pos.getZ()));
        }

        return sampleBiome(source, this.pos.getX(), this.pos.getY(), this.pos.getZ()) == this.biome;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Biome getBiome() {
        return this.biome;
    }

    public static Biome sampleBiome(FakeBiomeSource source, int x, int y, int z) {
        return VoronoiBiomeAccessType.INSTANCE.getBiome(source.getHashedSeed(), x, y, z, source);
    }

    @Override
    public void onDataAdded(DataStorage dataStorage) {
        dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof BiomeData) {
            return ((BiomeData)obj).biome == this.biome;
        }

        return false;
    }

    @Override
    public int hashCode() {
        if(this.biomePredicate == null) {
            return this.biome.getName().asFormattedString().hashCode();
        }

        return super.hashCode();
    }
}
