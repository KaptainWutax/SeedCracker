package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;

public class DesertWellData extends DecoratorData {

	public static final int SALT = 30010;
	private static final double BITS = Math.log(1000 * 16 * 16) / Math.log(2);
	private BlockPos pos;

	public DesertWellData(ChunkPos chunkPos, Biome biome, BlockPos pos) {
		super(chunkPos, SALT, biome);
		this.pos = new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
	}

	@Override
	public boolean testDecorator(Rand rand) {
		if(rand.nextFloat() >= 0.001F)return false;
		if(rand.nextInt(16) != this.pos.getX())return false;
		if(rand.nextInt(16) != this.pos.getZ())return false;
		return true;
	}

	@Override
	public double getBits() {
		return BITS;
	}

	@Override
	public void onDataAdded(DataStorage dataStorage) {
		dataStorage.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
	}

}
