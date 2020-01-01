package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.Decorator;

public class DesertWellData extends DecoratorData {

	private BlockPos pos;

	public DesertWellData(ChunkPos chunkPos, Biome biome, BlockPos pos) {
		super(chunkPos, Decorator.CHANCE_HEIGHTMAP, biome);
		this.pos = new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
	}

	@Override
	public boolean testDecorator(Rand rand) {
		if(rand.nextFloat() >= 0.001F)return false;
		if(rand.nextInt(16) != this.pos.getX())return false;
		if(rand.nextInt(16) != this.pos.getZ())return false;
		return true;
	}

}
