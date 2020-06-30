package kaptainwutax.seedcracker.cracker.decorator;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.VersionMap;

public class EndGateway extends Decorator<EndGateway.Config, EndGateway.Data> {

	public static final VersionMap<EndGateway.Config> CONFIGS = new VersionMap<EndGateway.Config>()
			.add(MCVersion.v1_13, new EndGateway.Config(3, 0, 700))
			.add(MCVersion.v1_16, new EndGateway.Config(4, 13, 700));

	public EndGateway(MCVersion version) {
		super(CONFIGS.getAsOf(version), version);
	}

	public EndGateway(EndGateway.Config config) {
		super(config, null);
	}

	@Override
	public String getName() {
		return "end_gateway";
	}

	@Override
	public boolean canStart(EndGateway.Data data, long structureSeed, ChunkRand rand) {
		super.canStart(data, structureSeed, rand);
		if(rand.nextInt(700) != 0)return false;
		if(rand.nextInt(16) != data.offsetX)return false;
		if(rand.nextInt(16) != data.offsetZ)return false;
		if(rand.nextInt(7) != data.height - 3)return false;
		return true;
	}

	@Override
	public boolean isValidBiome(Biome biome) {
		return biome == Biome.END_HIGHLANDS;
	}

	public EndGateway.Data at(int blockX, int blockZ, int height, Biome biome) {
		return new EndGateway.Data(this, blockX, blockZ, height, biome);
	}

	public static class Config extends Decorator.Config {
		public final int rarity;

		public Config(int step, int index, int rarity) {
			super(step, index);
			this.rarity = rarity;
		}
	}

	public static class Data extends Decorator.Data<EndGateway> {
		public final int offsetX;
		public final int offsetZ;
		public final int height;

		public Data(EndGateway feature, int blockX, int blockZ, int height, Biome biome) {
			super(feature, blockX >> 4, blockZ >> 4, biome);
			this.offsetX = blockX & 15;
			this.offsetZ = blockZ & 15;
			this.height = height;
		}
	}

 }
