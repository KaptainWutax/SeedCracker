package kaptainwutax.seedcracker.cracker.decorator;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.VersionMap;

public class DesertWell extends Decorator<Decorator.Config, DesertWell.Data> {

	public static final VersionMap<Decorator.Config> CONFIGS = new VersionMap<Decorator.Config>()
			.add(MCVersion.v1_13, new Decorator.Config(3, 1))
			.add(MCVersion.v1_16, new Decorator.Config(4, 13));

	public DesertWell(MCVersion version) {
		super(CONFIGS.getAsOf(version), version);
	}

	public DesertWell(Decorator.Config config) {
		super(config, null);
	}

	@Override
	public String getName() {
		return "desert_well";
	}

	@Override
	public boolean canStart(DesertWell.Data data, long structureSeed, ChunkRand rand) {
		super.canStart(data, structureSeed, rand);
		if(rand.nextFloat() >= 0.001F)return false;
		if(rand.nextInt(16) != data.offsetX)return false;
		if(rand.nextInt(16) != data.offsetZ)return false;
		return true;
	}

	@Override
	public boolean isValidBiome(Biome biome) {
		return biome == Biome.DESERT || biome == Biome.DESERT_HILLS || biome == Biome.DESERT_LAKES;
	}

	public DesertWell.Data at(int blockX, int blockZ, Biome biome) {
		return new DesertWell.Data(this, blockX, blockZ, biome);
	}

	public static class Data extends Decorator.Data<DesertWell> {
		public final int offsetX;
		public final int offsetZ;

		public Data(DesertWell feature, int blockX, int blockZ, Biome biome) {
			super(feature, blockX >> 4, blockZ >> 4, biome);
			this.offsetX = blockX & 15;
			this.offsetZ = blockZ & 15;
		}
	}

}
