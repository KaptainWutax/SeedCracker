package kaptainwutax.seedcracker.cracker.structure;

import kaptainwutax.seedcracker.cracker.structure.type.AbstractTempleType;
import kaptainwutax.seedcracker.cracker.structure.type.FeatureType;
import kaptainwutax.seedcracker.cracker.structure.type.RarityType;
import kaptainwutax.seedcracker.cracker.structure.type.TriangularType;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.Seeds;

public class StructureFeatures {

	public static final FeatureType<StructureData> DESERT_PYRAMID = new AbstractTempleType(14357617, 32, 24);

	public static final FeatureType<StructureData> IGLOO = new AbstractTempleType(14357618, 32, 24);

	public static final FeatureType<StructureData> JUNGLE_TEMPLE = new AbstractTempleType(14357619, 32, 24);

	public static final FeatureType<StructureData> SWAMP_HUT = new AbstractTempleType(14357620, 32, 24);

	public static final FeatureType<StructureData> OCEAN_RUIN = new AbstractTempleType(14357621, 16, 8);

	public static final FeatureType<StructureData> SHIPWRECK = new AbstractTempleType(165745295, 16, 8);

	public static final FeatureType<StructureData> PILLAGER_OUTPOST = new AbstractTempleType(165745296, 32, 24) {
		@Override
		public boolean test(Rand rand, StructureData data, long structureSeed) {
			if(!super.test(rand, data, structureSeed))return false;
			Seeds.setWeakSeed(rand, structureSeed, data.chunkX, data.chunkZ);
			return rand.nextInt(5) == 0;
		}
	};

	public static final FeatureType<StructureData> VILLAGE = new AbstractTempleType(10387312, 32, 24);

	public static final FeatureType<StructureData> END_CITY = new TriangularType(10387313, 20, 9);

	public static final FeatureType<StructureData> OCEAN_MONUMENT = new TriangularType(10387313, 32, 27);

	public static final FeatureType<StructureData> WOODLAND_MANSION = new TriangularType(10387319, 80, 60);

	public static final FeatureType<StructureData> BURIED_TREASURE = new RarityType(10387320, 1, 0.01F);

	public static final FeatureType<StructureData> NETHER_FORTRESS = new FeatureType<StructureData>(-1, 1) {
		@Override
		public boolean test(Rand rand, StructureData data, long structureSeed) {
			Seeds.setWeakSeed(rand, structureSeed, data.chunkX, data.chunkZ);

			return rand.nextInt(3) == 0
				&& data.chunkX == ((data.chunkX >> 4) << 4) + 4 + rand.nextInt(8)
				&& data.chunkZ == ((data.chunkZ >> 4) << 4) + 4 + rand.nextInt(8);
		}
	};

	public static final FeatureType<StructureData> MINESHAFT = new FeatureType<StructureData>(-1, 1) {
		@Override
		public boolean test(Rand rand, StructureData data, long structureSeed) {
			Seeds.setStructureStartSeed(rand, structureSeed, data.chunkX, data.chunkZ);
			return rand.nextDouble() < 0.004D;
		}
	};

}
