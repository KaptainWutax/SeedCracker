package kaptainwutax.seedcracker.cracker.structure.type;

import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.Seeds;
import net.minecraft.util.math.ChunkPos;

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
	public ChunkPos getInRegion(Rand rand, long structureSeed, int regionX, int regionZ) {
		Seeds.setRegionSeed(rand, structureSeed, regionX, regionZ, this.salt);
		if(rand.nextFloat() >= this.rarity)return null;
		return new ChunkPos(regionX, regionZ);
	}

	@Override
	public double getBits() {
		return this.bits;
	}

}
