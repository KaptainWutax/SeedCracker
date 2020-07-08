package kaptainwutax.seedcracker.util;

import net.minecraft.class_5458;
import net.minecraft.world.biome.Biomes;

public class BiomeFixer {

	public static kaptainwutax.biomeutils.Biome swap(net.minecraft.world.biome.Biome biome) {
		return kaptainwutax.biomeutils.Biome.REGISTRY.get(class_5458.field_25933.getRawId(biome));
	}

	public static net.minecraft.world.biome.Biome swap(kaptainwutax.biomeutils.Biome biome) {
		return Biomes.field_25821.get(biome.getId());
	}

}
