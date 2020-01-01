package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;

public class RarityType extends FeatureType<StructureData> {

	private float rarity;

	public RarityType(int salt, int distance, float rarity) {
		super(salt, distance);
		this.rarity = rarity;
	}

	@Override
	public boolean test(Rand rand, StructureData data, long structureSeed) {
		return rand.nextFloat() < this.rarity;
	}

}
