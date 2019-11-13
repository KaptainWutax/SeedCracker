package kaptainwutax.seedcracker.cracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PillarData {

    private List<Integer> heights;

    public PillarData(List<Integer> heights) {
        this.heights = heights;
    }

    public List<Integer> getPillarSeeds() {
        List<Integer> result = new ArrayList<>();

        for(int pillarSeed = 0; pillarSeed < (1 << 16); pillarSeed++) {
            List<Integer> h = this.getPillarHeights(pillarSeed);
            if(h.equals(heights))result.add(pillarSeed);
        }

        return result;
    }

    public List<Integer> getPillarHeights(int spikeSeed) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            indices.add(i);
        }

        Collections.shuffle(indices, new Random(spikeSeed));

        List<Integer> heights = new ArrayList<>();

        for (Integer index: indices) {
            heights.add(76 + index * 3);
        }

        return heights;
    }

}
