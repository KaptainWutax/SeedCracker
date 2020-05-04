package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.SeedData;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedutils.lcg.rand.JRand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PillarData extends SeedData {

	private List<Integer> heights;

	public PillarData(List<Integer> heights) {
		this.heights = heights;
	}

	@Override
	public boolean test(long seed, JRand rand) {
		List<Integer> h = this.getPillarHeights((int)seed);
		return h.equals(this.heights);
	}

	public List<Integer> getPillarHeights(int pillarSeed) {
		List<Integer> indices = new ArrayList<>();

		for(int i = 0; i < 10; i++) {
			indices.add(i);
		}

		Collections.shuffle(indices, new Random(pillarSeed));

		List<Integer> heights = new ArrayList<>();

		for(Integer index : indices) {
			heights.add(76 + index * 3);
		}

		return heights;
	}

	@Override
	public double getBits() {
		return 16;
	}

	@Override
	public void onDataAdded(DataStorage dataStorage) {
		dataStorage.getTimeMachine().poke(TimeMachine.Phase.PILLARS);
	}

}
