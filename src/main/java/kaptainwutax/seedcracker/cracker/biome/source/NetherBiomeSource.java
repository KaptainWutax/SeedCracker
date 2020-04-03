package kaptainwutax.seedcracker.cracker.biome.source;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;

public class NetherBiomeSource extends FixedBiomeSource implements IFakeBiomeSource {

	public NetherBiomeSource() {
		super(new FixedBiomeSourceConfig(null).setBiome(Biomes.NETHER));
	}

	@Override
	public long getWorldSeed() {
		throw new UnsupportedOperationException("Nether biome source doesn't use a seed");
	}

	@Override
	public long getHashedWorldSeed() {
		throw new UnsupportedOperationException("Nether biome source doesn't use a seed");
	}

	@Override
	public Biome sample(int x, int y, int z) {
		return Biomes.NETHER;
	}

	@Override
	public BiomeSource getBiomeSource() {
		return this;
	}

}
