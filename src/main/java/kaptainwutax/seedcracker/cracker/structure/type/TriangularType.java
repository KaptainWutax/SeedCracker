package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.seed.ChunkSeeds;
import net.minecraft.util.math.ChunkPos;

public class TriangularType extends FeatureType<StructureData> {

	protected final int peak;
	protected final double bits;

	public TriangularType(int salt, int distance, int separation) {
		super(salt, distance);
		this.peak = distance - separation;
		this.bits = Math.log(this.peak * this.peak) / Math.log(2);
	}

	@Override
	public boolean test(JRand rand, StructureData data, long structureSeed) {
		return (rand.nextInt(this.peak) + rand.nextInt(this.peak)) / 2 == data.offsetX
				&& (rand.nextInt(this.peak) + rand.nextInt(this.peak)) / 2 == data.offsetZ;
	}

	@Override
	public ChunkPos getInRegion(JRand rand, long structureSeed, int regionX, int regionZ) {
		long regionSeed = ChunkSeeds.getRegionSeed(structureSeed, regionX, regionZ, this.salt, MCVersion.v1_15);
		rand.setSeed(regionSeed);

		return new ChunkPos((rand.nextInt(this.peak) + rand.nextInt(this.peak)) / 2,
								(rand.nextInt(this.peak) + rand.nextInt(this.peak)));
	}

	/*
	 * For this type specifically, the bits approximation is bad since the distribution is special.
	 */
	@Override
	public double getBits() {
		return this.bits;
	}

}
