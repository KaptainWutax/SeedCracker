package kaptainwutax.seedcracker.cracker;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;

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

    public boolean test(FakeBiomeSource source) {
        return VoronoiBiomeAccessType.INSTANCE.getBiome(source.getHashedSeed(), this.x,0, this.z, source) == this.biome;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
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
