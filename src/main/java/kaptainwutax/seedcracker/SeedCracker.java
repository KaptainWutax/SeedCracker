package kaptainwutax.seedcracker;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.seedcracker.command.ClientCommand;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.mc.MCVersion;
import mjtb49.hashreversals.ChunkRandomReverser;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Formatting;

public class SeedCracker implements ModInitializer {

	public static MCVersion MC_VERSION = MCVersion.v1_16_2;

    private static final SeedCracker INSTANCE = new SeedCracker();
    private DataStorage dataStorage = new DataStorage();
	private boolean active = true;

	@Override
	public void onInitialize() {
		Features.init(MC_VERSION);
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public DataStorage getDataStorage() {
		return this.dataStorage;
	}

	public boolean isActive() {
		return this.active;
	}

    public void setActive(boolean active) {
		this.active = active;

	    if(this.active) {
		    ClientCommand.sendFeedback("SeedCracker is active.", Formatting.GREEN, true);
	    } else {
		    ClientCommand.sendFeedback("SeedCracker is not active.", Formatting.RED, true);
	    }
    }

	public void reset() {
		SeedCracker.get().getDataStorage().clear();
		FinderQueue.get().clear();
	}

	public static void main2(String[] args) {
		long[] decoratorSeeds = {70762812, 212622517, 257564993, 326374038, 393318946, 224099077, 374419999, 660875111, 1049761364, 1086454538};

		for(long decoratorSeed: decoratorSeeds) {
			long populationSeed = (decoratorSeed ^ LCG.JAVA.multiplier) - 80001L;
			ChunkRandomReverser r = new ChunkRandomReverser();
			r.reversePopulationSeed(populationSeed, 0, 0, MCVersion.v1_15).forEach(structureSeed -> {
				System.out.println("structure seed " + structureSeed);

				for(long upperBits = 0; upperBits < 1L << 8; upperBits++) {
					long worldSeed = (upperBits << 48) | structureSeed;
					OverworldBiomeSource ow = new OverworldBiomeSource(MCVersion.v1_15, worldSeed);
					if(ow.getBiomeForNoiseGen(2, 0, 2) == Biome.SAVANNA)System.out.println(worldSeed);
				}
			});
		}
	}

}
