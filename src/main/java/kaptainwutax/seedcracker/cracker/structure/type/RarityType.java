package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;

public class RarityType extends FeatureType<StructureData> {

	private final float rarity;
	private final double bits;

	public RarityType(int salt, int distance, float rarity) {
		super(salt, distance);
		this.rarity = rarity;
		this.bits = Math.log(1.0D / this.rarity) / Math.log(2);
	}

	@Override
	public boolean test(Rand rand, StructureData data, long structureSeed) {
		return rand.nextFloat() < this.rarity;
	}

	@Override
	public double getBits() {
		return this.bits;
	}

}
