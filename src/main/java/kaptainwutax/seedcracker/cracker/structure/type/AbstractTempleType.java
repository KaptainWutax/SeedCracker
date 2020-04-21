package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.Seeds;
import net.minecraft.util.math.ChunkPos;

public class AbstractTempleType extends FeatureType<StructureData> {

	protected final int offset;
	protected final double bits;

	public AbstractTempleType(int salt, int distance, int offset) {
		super(salt, distance);
		this.offset = offset;
		this.bits = Math.log(this.offset * this.offset) / Math.log(2);
	}

	@Override
	public boolean test(Rand rand, StructureData data, long structureSeed) {
		return rand.nextInt(this.offset) == data.offsetX && rand.nextInt(this.offset) == data.offsetZ;
	}

	@Override
	public ChunkPos getInRegion(Rand rand, long structureSeed, int regionX, int regionZ) {
		Seeds.setRegionSeed(rand, structureSeed, regionX, regionZ, this.salt);
		return new ChunkPos(regionX * this.distance + rand.nextInt(this.offset), regionZ * this.distance + rand.nextInt(this.offset));
	}

	@Override
	public double getBits() {
		return this.bits;
	}

}
