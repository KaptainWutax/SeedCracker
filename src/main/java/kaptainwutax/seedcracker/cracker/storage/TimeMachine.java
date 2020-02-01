package kaptainwutax.seedcracker.cracker.storage;

import kaptainwutax.seedcracker.cracker.BiomeData;
import kaptainwutax.seedcracker.cracker.FakeBiomeSource;
import kaptainwutax.seedcracker.util.Log;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeMachine {

	public static ExecutorService SERVICE = Executors.newFixedThreadPool(1);

	private LCG inverseLCG = Rand.JAVA_LCG.combine(-2);
	protected DataStorage dataStorage;

	public boolean isRunning = false;
	public boolean terminate = false;

	protected List<Integer> pillarSeeds = null;
	protected List<Long> structureSeeds = null;
	protected List<Long> worldSeeds = null;

	public TimeMachine(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
	}

	public void poke(Phase phase) {
		this.isRunning = true;
		final Phase[] finalPhase = {phase};

		SERVICE.submit(() -> {
			while(finalPhase[0] != null && !this.terminate) {
				if(finalPhase[0] == Phase.PILLARS) {
					if(!this.pokePillars())break;
				} else if(finalPhase[0] == Phase.STRUCTURES) {
					if(!this.pokeStructures())break;
				} else if(finalPhase[0] == Phase.BIOMES) {
					if(!this.pokeBiomes())break;
				}

				finalPhase[0] = finalPhase[0].nextPhase();
			}
		});

		this.isRunning = false;
	}

	protected boolean pokePillars() {
		if(this.pillarSeeds != null || this.dataStorage.pillarData == null)return false;
		this.pillarSeeds = new ArrayList<>();

		Log.debug("====================================");
		Log.warn("Looking for pillar seeds...");

		for(int pillarSeed = 0; pillarSeed < 1 << 16 && !this.terminate; pillarSeed++) {
			if(this.dataStorage.pillarData.test(pillarSeed, null)) {
				Log.warn("Found pillar seed [" + pillarSeed + "].");
				this.pillarSeeds.add(pillarSeed);
			}
		}

		if(!this.pillarSeeds.isEmpty()) {
			Log.warn("Finished searching for pillar seeds.");
		} else {
			Log.error("Finished search with no results.");
		}

		return true;
	}

	protected boolean pokeStructures() {
		if(this.pillarSeeds == null || this.structureSeeds != null || this.dataStorage.getBaseBits() < 54.0D)return false;
		this.structureSeeds = new ArrayList<>();

		Rand rand = new Rand(0L, false);
		Log.debug("====================================");
		Log.warn("Looking for structure seeds...");

		for(int pillarSeed: this.pillarSeeds) {
			for(long partialWorldSeed = 0; partialWorldSeed <  1L << 32; partialWorldSeed++) {
				if((partialWorldSeed & ((1 << 29) - 1)) == 0) {
					Log.debug("Progress: " + ((100.0D * (double)partialWorldSeed) / (double)(1L << 32)) + "%");
				}

				long seed = this.timeMachine(partialWorldSeed, pillarSeed);
				boolean matches = true;

				for(SeedData baseSeedDatum: this.dataStorage.baseSeedData) {
					if(!baseSeedDatum.test(seed, rand)) {
						matches = false;
						break;
					}
				}

				if(matches) {
					this.structureSeeds.add(seed);
					Log.warn("Found structure seed [" + seed + "].");
				}

				if(this.terminate) {
					return false;
				}
			}
		}

		if(!this.structureSeeds.isEmpty()) {
			Log.warn("Finished searching for structure seeds.");
		} else {
			Log.error("Finished search with no results.");
		}

		return true;
	}

	protected boolean pokeBiomes() {
		if(this.structureSeeds == null || this.worldSeeds != null)return false;
		if(this.dataStorage.hashedSeedData == null && this.dataStorage.biomeSeedData.size() < 5)return false;

		this.worldSeeds = new ArrayList<>();
		Log.debug("====================================");
		Log.warn("Looking for world seeds...");

		if(this.dataStorage.hashedSeedData != null) {
			for(long structureSeed: this.structureSeeds) {
				for(long upperBits = 0; upperBits < 1 << 16; upperBits++) {
					long worldSeed = (upperBits << 48) | structureSeed;

					if(!this.dataStorage.hashedSeedData.test(worldSeed, null)) {
						continue;
					} else {
						this.worldSeeds.add(worldSeed);
						Log.warn("Found world seed [" + worldSeed + "]");
					}

					if(this.terminate) {
						return false;
					}
				}
			}

			if(!this.worldSeeds.isEmpty()) {
				Log.warn("Finished searching for world seeds.");
				return true;
			} else {
				Log.error("Finished search with no results, reverting back to biomes.");
			}
		}

		Log.warn("Looking for world seeds...");

		for(long structureSeed: this.structureSeeds) {
			for(long upperBits = 0; upperBits < 1 << 16; upperBits++) {
				long worldSeed = (upperBits << 48) | structureSeed;

				FakeBiomeSource source = new FakeBiomeSource(worldSeed);
				boolean matches = true;

				for(BiomeData biomeSeedDatum: this.dataStorage.biomeSeedData) {
					if(!biomeSeedDatum.test(source)) {
						matches = false;
						break;
					}
				}

				if(matches) {
					this.worldSeeds.add(worldSeed);
					Log.warn("Found world seed [" + worldSeed + "].");
				}

				if(this.terminate) {
					return false;
				}
			}
		}

		if(!this.worldSeeds.isEmpty()) {
			Log.warn("Finished searching for world seeds.");
		} else {
			Log.error("Finished search with no results.");
		}

		return true;
	}

	public long timeMachine(long partialWorldSeed, int pillarSeed) {
		long currentSeed = 0L;
		currentSeed |= (partialWorldSeed & 0xFFFF0000L) << 16;
		currentSeed |= (long)pillarSeed << 16;
		currentSeed |= partialWorldSeed & 0xFFFFL;

		currentSeed = this.inverseLCG.nextSeed(currentSeed);
		currentSeed ^= Rand.JAVA_LCG.multiplier;
		return currentSeed;
	}

	public enum Phase {
		BIOMES(null), STRUCTURES(BIOMES), PILLARS(STRUCTURES);

		private final Phase nextPhase;

		Phase(Phase nextPhase) {
			this.nextPhase = nextPhase;
		}

		public Phase nextPhase() {
			return this.nextPhase;
		}
	}

}
