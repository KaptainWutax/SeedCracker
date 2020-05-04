package kaptainwutax.seedcracker.cracker.structure;

import kaptainwutax.seedcracker.cracker.structure.type.*;
import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.seed.ChunkSeeds;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

public class StructureFeatures {

	public static final ChunkGeneratorConfig CONFIG = new ChunkGeneratorConfig();

	public static final FeatureType<StructureData> DESERT_PYRAMID = new TempleType(14357617);

	public static final FeatureType<StructureData> IGLOO = new TempleType(14357618);

	public static final FeatureType<StructureData> JUNGLE_TEMPLE = new TempleType(14357619);

	public static final FeatureType<StructureData> SWAMP_HUT = new TempleType(14357620);

	public static final FeatureType<StructureData> OCEAN_RUIN = new RegionType(14357621, CONFIG.getOceanRuinSpacing(), CONFIG.getOceanRuinSeparation());

	public static final FeatureType<StructureData> SHIPWRECK = new RegionType(165745295, CONFIG.getShipwreckSpacing(), CONFIG.getShipwreckSeparation());

	public static final FeatureType<StructureData> PILLAGER_OUTPOST = new TempleType(165745296) {
		@Override
		public boolean test(JRand rand, StructureData data, long structureSeed) {
			if(!super.test(rand, data, structureSeed))return false;
			long seed = ChunkSeeds.getWeakSeed(structureSeed, data.chunkX, data.chunkZ, MCVersion.v1_15);
			rand.setSeed(seed);
			rand.nextInt(); //Mojang... why?
			return rand.nextInt(5) == 0;
		}

		@Override
		public ChunkPos getInRegion(JRand rand, long structureSeed, int regionX, int regionZ) {
			ChunkPos pos = super.getInRegion(rand, structureSeed, regionX, regionZ);
			long seed = ChunkSeeds.getWeakSeed(structureSeed, pos.x, pos.z, MCVersion.v1_15);
			rand.setSeed(seed);
			rand.nextInt(); //Mojang... why?
			return rand.nextInt(5) == 0 ? pos : null;
		}
	};

	public static final FeatureType<StructureData> VILLAGE = new TempleType(10387312);

	public static final FeatureType<StructureData> END_CITY = new TriangularType(10387313, CONFIG.getEndCityDistance(), CONFIG.getEndCitySeparation());

	public static final FeatureType<StructureData> OCEAN_MONUMENT = new TriangularType(10387313, CONFIG.getOceanMonumentSpacing(), CONFIG.getOceanMonumentSeparation());

	public static final FeatureType<StructureData> WOODLAND_MANSION = new TriangularType(10387319, CONFIG.getMansionDistance(), CONFIG.getMansionSeparation());

	public static final FeatureType<StructureData> BURIED_TREASURE = new RarityType(10387320, 0.01F);

	public static final FeatureType<StructureData> NETHER_FORTRESS = new RegionType(CONFIG.getNetherStructureSeedModifier(), CONFIG.getNetherStructureSpacing(), CONFIG.getNetherStructureSeparation()) {
		protected final double bits = Math.log(3) / Math.log(2);

		@Override
		public boolean test(JRand rand, StructureData data, long structureSeed) {
			return super.test(rand, data, structureSeed) && rand.nextInt(6) < 2;
		}

		@Override
		public ChunkPos getInRegion(JRand rand, long structureSeed, int regionX, int regionZ) {
			ChunkPos pos = super.getInRegion(rand, structureSeed, regionX, regionZ);
			return rand.nextInt(6) < 2 ? pos : null;
		}

		@Override
		public double getBits() {
			return super.getBits() + this.bits;
		}
	};

	public static final FeatureType<StructureData> MINESHAFT = new FeatureType<StructureData>(-1, 1) {
		protected final double bits = Math.log(1.0D / 0.004D) / Math.log(2);

		@Override
		public boolean test(JRand rand, StructureData data, long structureSeed) {
			long seed = ChunkSeeds.getCarverSeed(structureSeed, data.chunkX, data.chunkZ, MCVersion.v1_15);
			rand.setSeed(seed);
			return rand.nextDouble() < 0.004D;
		}

		@Override
		public ChunkPos getInRegion(JRand rand, long structureSeed, int regionX, int regionZ) {
			long seed = ChunkSeeds.getCarverSeed(structureSeed, regionX, regionZ, MCVersion.v1_15);
			rand.setSeed(seed);
			return rand.nextDouble() >= 0.004D ? null : new ChunkPos(regionX, regionZ);
		}

		@Override
		public double getBits() {
			return this.bits;
		}
	};

}
