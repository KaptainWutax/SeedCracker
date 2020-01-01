package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;

public class AbstractTempleType extends FeatureType<StructureData> {

	protected final int offset;

	public AbstractTempleType(int salt, int distance, int offset) {
		super(salt, distance);
		this.offset = offset;
	}

	@Override
	public boolean test(Rand rand, StructureData data, long structureSeed) {
		return rand.nextInt(this.offset) == data.offsetX && rand.nextInt(this.offset) == data.offsetZ;
	}

}
