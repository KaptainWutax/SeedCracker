package kaptainwutax.seedcracker.cracker.biome;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.LevelGeneratorType;

import java.util.Set;

public class GeneratorTypeData {

	private static final Set<LevelGeneratorType> WHITELIST = ImmutableSet.of(
			LevelGeneratorType.DEFAULT, LevelGeneratorType.DEFAULT_1_1,
			LevelGeneratorType.AMPLIFIED, LevelGeneratorType.LARGE_BIOMES
	);

	private final LevelGeneratorType generatorType;
	private final boolean isSupported;

	public GeneratorTypeData(LevelGeneratorType generatorType) {
		this.generatorType = generatorType;
		this.isSupported = isSupported(generatorType);
	}

	public LevelGeneratorType getGeneratorType() {
		return this.generatorType;
	}

	public boolean isSupported() {
		return this.isSupported;
	}

	public static boolean isSupported(LevelGeneratorType generatorType) {
		return WHITELIST.contains(generatorType);
	}

}
