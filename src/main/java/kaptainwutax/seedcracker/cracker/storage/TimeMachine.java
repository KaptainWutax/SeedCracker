package kaptainwutax.seedcracker.cracker.storage;

import kaptainwutax.seedcracker.cracker.biome.BiomeData;
import kaptainwutax.seedcracker.cracker.biome.source.OverworldBiomeSource;
import kaptainwutax.seedcracker.util.Log;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeMachine {

	public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

	private LCG inverseLCG = Rand.JAVA_LCG.combine(-2);
	protected DataStorage dataStorage;

	public boolean isRunning = false;
	public boolean shouldTerminate = false;

	public List<Integer> pillarSeeds = null;
	public List<Long> structureSeeds = null;
	public List<Long> worldSeeds = null;

	public TimeMachine(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
	}

	public void poke(Phase phase) {
		this.isRunning = true;

		final Phase[] finalPhase = {phase};

		while(finalPhase[0] != null && !this.shouldTerminate) {
			if(finalPhase[0] == Phase.PILLARS) {
				if(!this.pokePillars())break;
			} else if(finalPhase[0] == Phase.STRUCTURES) {
				if(!this.pokeStructures())break;
			} else if(finalPhase[0] == Phase.BIOMES) {
				if(!this.pokeBiomes())break;
			}

			finalPhase[0] = finalPhase[0].nextPhase();
		}
	}

	protected boolean pokePillars() {
		if(this.pillarSeeds != null || this.dataStorage.pillarData == null)return false;
		this.pillarSeeds = new ArrayList<>();

		Log.debug("====================================");
		Log.warn("Looking for pillar seeds...");

		for(int pillarSeed = 0; pillarSeed < 1 << 16 && !this.shouldTerminate; pillarSeed++) {
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
		if(this.pillarSeeds == null || this.structureSeeds != null ||
				this.dataStorage.getBaseBits() < this.dataStorage.getWantedBits())return false;

		this.structureSeeds = new ArrayList<>();

		SeedData[] cache = new SeedData[this.dataStorage.baseSeedData.size()];
		int id = 0;

		for(SeedData baseSeedDatum: this.dataStorage.baseSeedData) {
			cache[id++] = baseSeedDatum;
		}

		for(int pillarSeed: this.pillarSeeds) {
			Log.debug("====================================");
			Log.warn("Looking for structure seeds with pillar seed [" + pillarSeed + "]...");

			AtomicInteger completion = new AtomicInteger();
			ProgressListener progressListener = new ProgressListener();

			for(int threadId = 0; threadId < 4; threadId++) {
				int fThreadId = threadId;

				SERVICE.submit(() -> {
					Rand rand = new Rand(0L, false);

					long lower = (long)fThreadId * (1L << 30);
					long upper = (long)(fThreadId + 1) * (1L << 30);

					for(long partialWorldSeed = lower; partialWorldSeed < upper && !this.shouldTerminate; partialWorldSeed++) {
						if((partialWorldSeed & ((1 << 27) - 1)) == 0) {
							progressListener.addPercent(3.125F, true);
						}

						long seed = this.timeMachine(partialWorldSeed, pillarSeed);

						boolean matches = true;

						for(SeedData baseSeedDatum: cache) {
							if(!baseSeedDatum.test(seed, rand)) {
								matches = false;
								break;
							}
						}

						if(matches) {
							this.structureSeeds.add(seed);
							Log.warn("Found structure seed [" + seed + "].");
						}
					}

					completion.getAndIncrement();
				});
			}

			while(completion.get() != 4) {
				try {Thread.sleep(50);}
				catch(InterruptedException e) {e.printStackTrace();}

				if(this.shouldTerminate) {
					return false;
				}
			}

			progressListener.addPercent(0.0F, true);
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
		if(this.dataStorage.hashedSeedData == null &&
				(this.dataStorage.biomeSeedData.size() < 5 || !this.dataStorage.generatorTypeData.isSupported()))return false;

		this.worldSeeds = new ArrayList<>();
		Log.debug("====================================");

		if(this.dataStorage.hashedSeedData != null) {
			Log.warn("Looking for world seeds...");

			for(long structureSeed: this.structureSeeds) {
				for(long upperBits = 0; upperBits < 1 << 16; upperBits++) {
					long worldSeed = (upperBits << 48) | structureSeed;

					if(!this.dataStorage.hashedSeedData.test(worldSeed, null)) {
						continue;
					} else {
						this.worldSeeds.add(worldSeed);
						Log.warn("Found world seed [" + worldSeed + "].");
					}

					if(this.shouldTerminate) {
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

		if(this.dataStorage.generatorTypeData.isSupported()) {
			Log.warn("Looking for world seeds...");

			for(long structureSeed : this.structureSeeds) {
				for(long upperBits = 0; upperBits < 1 << 16; upperBits++) {
					long worldSeed = (upperBits << 48) | structureSeed;

					OverworldBiomeSource source = new OverworldBiomeSource(
							worldSeed, this.dataStorage.generatorTypeData.getGeneratorType()
					);

					boolean matches = true;

					for(BiomeData biomeSeedDatum : this.dataStorage.biomeSeedData) {
						if(!biomeSeedDatum.test(source)) {
							matches = false;
							break;
						}
					}

					if(matches) {
						this.worldSeeds.add(worldSeed);
						Log.warn("Found world seed [" + worldSeed + "].");
					}

					if(this.shouldTerminate) {
						return false;
					}
				}
			}

			if(!this.worldSeeds.isEmpty()) {
				Log.warn("Finished searching for world seeds.");
			} else {
				Log.error("Finished search with no results.");
			}
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
