package kaptainwutax.seedcracker.cracker.biome.source;

import net.minecraft.class_5217;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.level.LevelGeneratorType;

public class OverworldBiomeSource extends VanillaLayeredBiomeSource implements IFakeBiomeSource {

	private final long worldSeed;
	private final long hashedWorldSeed;
	private final LevelGeneratorType generatorType;

	public OverworldBiomeSource(long worldSeed, LevelGeneratorType generatorType) {
		super(new VanillaLayeredBiomeSourceConfig(worldSeed));
		this.worldSeed = worldSeed;
		this.hashedWorldSeed = class_5217.method_27418(worldSeed);
		this.generatorType = generatorType;
	}

	@Override
	public long getWorldSeed() {
		return this.worldSeed;
	}

	@Override
	public long getHashedWorldSeed() {
		return this.hashedWorldSeed;
	}

	public LevelGeneratorType getGeneratorType() {
		return this.generatorType;
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
