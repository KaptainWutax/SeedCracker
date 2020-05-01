package kaptainwutax.seedcracker.cracker.biome.source;

import net.minecraft.class_5217;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceConfig;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;

public class NetherBiomeSource extends MultiNoiseBiomeSource implements IFakeBiomeSource {

	private final long worldSeed;
	private final long hashedWorldSeed;

	public NetherBiomeSource(long worldSeed) {
		this(worldSeed, class_5217.method_27418(worldSeed));
	}

	public NetherBiomeSource(long worldSeed, long hashedWorldSeed) {
		super(new MultiNoiseBiomeSourceConfig(worldSeed));
		this.worldSeed = worldSeed;
		this.hashedWorldSeed = hashedWorldSeed;
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
		return VoronoiBiomeAccessType.INSTANCE.getBiome(this.getHashedWorldSeed(), x, y, z, this);
	}

	@Override
	public BiomeSource getBiomeSource() {
		return this;
	}

}
