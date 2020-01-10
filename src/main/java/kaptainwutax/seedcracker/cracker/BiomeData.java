package kaptainwutax.seedcracker.cracker;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;

import java.util.function.Predicate;

public class BiomeData {

    private BlockPos pos;

    private Biome biome;
    private Predicate<Biome> biomePredicate;

    public BiomeData(BlockPos pos, Biome biome) {
        this.pos = pos;
        this.biome = biome;
    }

    public BiomeData(BlockPos pos, Predicate<Biome> biomePredicate) {
        this.pos = pos;
        this.biomePredicate = biomePredicate;
    }

    public boolean test(FakeBiomeSource source) {
        if(this.biome == null) {
            return this.biomePredicate.test(VoronoiBiomeAccessType.INSTANCE.getBiome(source.getHashedSeed(), this.pos.getX(),this.pos.getY(), this.pos.getZ(), source));
        }

        return VoronoiBiomeAccessType.INSTANCE.getBiome(source.getHashedSeed(), this.pos.getX(),this.pos.getY(), this.pos.getZ(), source) == this.biome;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Biome getBiome() {
        return this.biome;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof BiomeData) {
            return ((BiomeData)obj).biome == this.biome;
        }

        return false;
    }

}
