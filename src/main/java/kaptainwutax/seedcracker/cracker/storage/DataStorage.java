package kaptainwutax.seedcracker.cracker.storage;

import io.netty.util.internal.ConcurrentSet;
import kaptainwutax.seedcracker.cracker.BiomeData;
import kaptainwutax.seedcracker.cracker.PillarData;
import kaptainwutax.seedcracker.cracker.structure.StructureData;
import kaptainwutax.seedcracker.util.Log;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;

public class DataStorage {

	public static Comparator<SeedData> SEED_DATA_COMPARATOR = (s1, s2) -> {
		boolean isStructure1 = s1 instanceof StructureData;
		boolean isStructure2 = s2 instanceof StructureData;

		if(isStructure1 != isStructure2) {
			return isStructure2 ? 1: -1;
		}

		if(s1.equals(s2)) {
			return 0;
		}

		double diff = s2.getBits() - s1.getBits();
		return diff == 0.0D ? 1 : (int)Math.signum(diff);
	};

	protected TimeMachine timeMachine = new TimeMachine(this);
	protected Set<Consumer<DataStorage>> scheduledData = new ConcurrentSet<>();

	protected PillarData pillarData = null;
	protected ScheduledSet<SeedData> baseSeedData = new ScheduledSet<>(SEED_DATA_COMPARATOR);
	protected ScheduledSet<BiomeData> biomeSeedData = new ScheduledSet<>(null);
	protected HashedSeedData hashedSeedData = null;

	public void tick() {
		if(!this.timeMachine.isRunning) {
			this.baseSeedData.dump();
			this.biomeSeedData.dump();

			this.scheduledData.removeIf(c -> {
				c.accept(this);
				return true;
			});
		}
	}

	public synchronized boolean addPillarData(PillarData pillarData) {
		boolean isAdded = this.pillarData == null;

		if(isAdded && pillarData != null) {
			this.pillarData = pillarData;
			this.schedule(pillarData::onDataAdded);
		}

		return isAdded;
	}

	public synchronized boolean addBaseData(SeedData seedData) {
		if(this.baseSeedData.contains(seedData)) {
			return false;
		}

		this.baseSeedData.scheduleAdd(seedData);
		this.schedule(seedData::onDataAdded);
		return true;
	}

	public synchronized boolean addBiomeData(BiomeData biomeData) {
		if(this.biomeSeedData.contains(biomeData)) {
			return false;
		}

		this.biomeSeedData.scheduleAdd(biomeData);
		this.schedule(biomeData::onDataAdded);
		return true;
	}

	public synchronized boolean addHashedSeedData(HashedSeedData hashedSeedData) {
		if(this.hashedSeedData == null || this.hashedSeedData.getHashedSeed() != hashedSeedData.getHashedSeed()) {
			Log.warn("Fetched hashed world seed [" + hashedSeedData.getHashedSeed() + "].");
			this.hashedSeedData = hashedSeedData;
			this.schedule(hashedSeedData::onDataAdded);
			return true;
		}

		return false;
	}

	public void schedule(Consumer<DataStorage> consumer) {
		this.scheduledData.add(consumer);
	}

	public TimeMachine getTimeMachine() {
		return this.timeMachine;
	}

	public double getBaseBits() {
		double bits = 0.0D;

		for(SeedData baseSeedDatum: this.baseSeedData) {
			bits += baseSeedDatum.getBits();
		}

		return bits;
	}

	public void clear() {
		System.out.println("Clearing data storage.");
		this.scheduledData = new ConcurrentSet<>();

		this.pillarData = null;
		this.baseSeedData = new ScheduledSet<>(SEED_DATA_COMPARATOR);
		this.biomeSeedData = new ScheduledSet<>(null);
		this.hashedSeedData = null;
		this.timeMachine.terminate = true;
		this.timeMachine = new TimeMachine(this);
	}

}
