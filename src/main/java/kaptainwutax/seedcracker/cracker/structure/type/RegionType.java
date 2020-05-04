package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.seed.ChunkSeeds;
import net.minecraft.util.math.ChunkPos;

public class RegionType extends FeatureType<StructureData> {

	protected final int offset;
	protected final double bits;

	public RegionType(int salt, int spacing, int separation) {
		super(salt, spacing);
		this.offset = spacing - separation;
		this.bits = Math.log(this.offset * this.offset) / Math.log(2);
	}

	@Override
	public boolean test(JRand rand, StructureData data, long structureSeed) {
		return rand.nextInt(this.offset) == data.offsetX && rand.nextInt(this.offset) == data.offsetZ;
	}

	@Override
	public ChunkPos getInRegion(JRand rand, long structureSeed, int regionX, int regionZ) {
		long regionSeed = ChunkSeeds.getRegionSeed(structureSeed, regionX, regionZ, this.salt, MCVersion.v1_15);
		rand.setSeed(regionSeed);
		return new ChunkPos(regionX * this.distance + rand.nextInt(this.offset), regionZ * this.distance + rand.nextInt(this.offset));
	}

	@Override
	public double getBits() {
		return this.bits;
	}

}
