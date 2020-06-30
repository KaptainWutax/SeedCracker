package kaptainwutax.seedcracker.cracker.misc;

import net.minecraft.util.registry.Registry;

public class BiomeFixer {

	public static kaptainwutax.biomeutils.Biome swap(net.minecraft.world.biome.Biome biome) {
		return kaptainwutax.biomeutils.Biome.REGISTRY.get(Registry.BIOME.getRawId(biome));
	}

	public static net.minecraft.world.biome.Biome swap(kaptainwutax.biomeutils.Biome biome) {
		return Registry.BIOME.get(biome.getId());
	}

}
