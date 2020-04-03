package kaptainwutax.seedcracker.cracker.biome;

import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelProperties;

import java.lang.reflect.Field;

public class FakeLevelProperties extends LevelProperties {

	public static final FakeLevelProperties INSTANCE = new FakeLevelProperties();

	private static Field RANDOM_SEED = LevelProperties.class.getDeclaredFields()[4];
	private static Field GENERATOR_TYPE = LevelProperties.class.getDeclaredFields()[5];

	static {
		RANDOM_SEED.setAccessible(true);
		GENERATOR_TYPE.setAccessible(true);
	}

	public FakeLevelProperties() {

	}

	public FakeLevelProperties loadProperties(long worldSeed, LevelGeneratorType generatorType) {
		try {
			RANDOM_SEED.set(this, worldSeed);
			GENERATOR_TYPE.set(this, generatorType);
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}

		return this;
	}

}
