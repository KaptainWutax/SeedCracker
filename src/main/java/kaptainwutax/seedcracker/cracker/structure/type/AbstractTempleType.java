package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;

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
	public double getBits() {
		return this.bits;
	}

}
