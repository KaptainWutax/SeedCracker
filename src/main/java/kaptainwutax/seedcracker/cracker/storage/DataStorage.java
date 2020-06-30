package kaptainwutax.seedcracker.cracker.storage;

import io.netty.util.internal.ConcurrentSet;
import kaptainwutax.featureutils.Feature;
import kaptainwutax.featureutils.structure.BuriedTreasure;
import kaptainwutax.featureutils.structure.Structure;
import kaptainwutax.featureutils.structure.TriangularStructure;
import kaptainwutax.featureutils.structure.UniformStructure;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.cracker.misc.PillarData;
import kaptainwutax.seedcracker.cracker.misc.BiomeData;
import kaptainwutax.seedcracker.cracker.misc.HashedSeedData;
import kaptainwutax.seedcracker.cracker.decorator.DesertWell;
import kaptainwutax.seedcracker.cracker.decorator.Dungeon;
import kaptainwutax.seedcracker.cracker.decorator.EmeraldOre;
import kaptainwutax.seedcracker.cracker.decorator.EndGateway;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;

public class DataStorage {

	public static final DataAddedEvent POKE_PILLARS = s -> s.timeMachine.poke(TimeMachine.Phase.PILLARS);
	public static final DataAddedEvent POKE_STRUCTURES = s -> s.timeMachine.poke(TimeMachine.Phase.STRUCTURES);
	public static final DataAddedEvent POKE_BIOMES = s -> s.timeMachine.poke(TimeMachine.Phase.BIOMES);

	public static final Comparator<Entry> SEED_DATA_COMPARATOR = (s1, s2) -> {
		boolean isStructure1 = s1.data.feature instanceof Structure;
		boolean isStructure2 = s2.data.feature instanceof Structure;

		//Structures always come before decorators.
		if(isStructure1 != isStructure2) {
			return isStructure2 ? 1: -1;
		}

		if(s1.equals(s2)) {
			return 0;
		}

		double diff = getBits(s2.data.feature) - getBits(s1.data.feature);
		return diff == 0 ? 1 : (int)Math.signum(diff);
	};

	protected TimeMachine timeMachine = new TimeMachine(this);
	protected Set<Consumer<DataStorage>> scheduledData = new ConcurrentSet<>();

	protected PillarData pillarData = null;
	protected ScheduledSet<Entry> baseSeedData = new ScheduledSet<>(SEED_DATA_COMPARATOR);
	protected ScheduledSet<BiomeData> biomeSeedData = new ScheduledSet<>(null);
	protected HashedSeedData hashedSeedData = null;

	public void tick() {
		if(!this.timeMachine.isRunning) {
			this.baseSeedData.dump();
			this.biomeSeedData.dump();

			this.timeMachine.isRunning = true;

			TimeMachine.SERVICE.submit(() -> {
				try {
					this.scheduledData.removeIf(c -> {
						c.accept(this);
						return true;
					});
				} catch(Exception e) {
					e.printStackTrace();
				}

				this.timeMachine.isRunning = false;
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

	public synchronized boolean addBaseData(Feature.Data<?> data, DataAddedEvent event) {
		Entry e = new Entry(data, event);

		if(this.baseSeedData.contains(e)) {
			return false;
		}

		this.baseSeedData.scheduleAdd(e);
		this.schedule(event::onDataAdded);
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

		for(Entry e: this.baseSeedData) {
			bits += getBits(e.data.feature);
		}

		return bits;
	}

	public double getWantedBits() {
		return 32.0D;
	}

	public static double getBits(Feature<?, ?> feature) {
		if(feature instanceof UniformStructure) {
			UniformStructure<?> s = (UniformStructure<?>)feature;
			return Math.log(s.getOffset() * s.getOffset()) / Math.log(2);
		} else if(feature instanceof TriangularStructure) {
			TriangularStructure<?> s = (TriangularStructure<?>)feature;
			return Math.log(s.getPeak() * s.getPeak()) / Math.log(2);
		}

		if(feature instanceof BuriedTreasure)return Math.log(100) / Math.log(2);
		if(feature instanceof DesertWell)return Math.log(1000 * 16 * 16) / Math.log(2);
		if(feature instanceof Dungeon)return Math.log(256 * 16 * 16 * 0.125D) / Math.log(2);
		if(feature instanceof EmeraldOre)return Math.log(28 * 16 * 16 * 0.5D) / Math.log(2);
		if(feature instanceof EndGateway)return Math.log(700 * 16 * 16 * 7) / Math.log(2);

		throw new UnsupportedOperationException("go do implement bits count for " + feature.getName() + " you fool");
	}

	public void clear() {
		this.scheduledData = new ConcurrentSet<>();
		this.pillarData = null;
		this.baseSeedData = new ScheduledSet<>(SEED_DATA_COMPARATOR);
		this.biomeSeedData = new ScheduledSet<>(null);
		this.hashedSeedData = null;
		this.timeMachine.shouldTerminate = true;
		this.timeMachine = new TimeMachine(this);
	}

	public static class Entry {
		public final Feature.Data<?> data;
		public final DataAddedEvent event;

		public Entry(Feature.Data<?> data, DataAddedEvent event) {
			this.data = data;
			this.event = event;
		}

		@Override
		public boolean equals(Object o) {
			if(this == o)return true;
			if(!(o instanceof Entry))return false;
			Entry entry = (Entry)o;
			return data.feature == entry.data.feature
					&& data.chunkX == entry.data.chunkX && data.chunkZ == entry.data.chunkZ;
		}
	}

}
