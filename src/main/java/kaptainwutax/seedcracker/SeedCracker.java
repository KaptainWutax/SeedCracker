package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.cracker.BiomeData;
import kaptainwutax.seedcracker.cracker.FakeBiomeSource;
import kaptainwutax.seedcracker.cracker.SpawnPointData;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.cracker.structure.StructureFeatures;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.magic.PopulationReversal;
import kaptainwutax.seedcracker.magic.RandomSeed;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;
import kaptainwutax.stronghold.Main;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.Feature;

public class SeedCracker implements ModInitializer {

    private static final SeedCracker INSTANCE = new SeedCracker();
    private DataStorage dataStorage = new DataStorage();

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

    public DataStorage getDataStorage() {
		return this.dataStorage;
	}

	public static void main(String[] args) {
		int pillarSeed = 6739;
		long target = 2853180570590L;

		TimeMachine timeMachine = new TimeMachine(null);

		for(long i = 0; i < 1L << 32; i++) {
			long seed = timeMachine.timeMachine(i, pillarSeed);

			if(seed == target) {
				System.out.println(i);
			}
		}

		System.out.println("Finished.");
	}

}
