package kaptainwutax.seedcracker.cracker.biome.source;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public interface IFakeBiomeSource {

	long getWorldSeed();

	long getHashedWorldSeed();

	Biome sample(int x, int y, int z);

	default Biome sample(BlockPos pos) {
		return this.sample(pos.getX(), pos.getY(), pos.getZ());
	}

	BiomeSource getBiomeSource();

}
