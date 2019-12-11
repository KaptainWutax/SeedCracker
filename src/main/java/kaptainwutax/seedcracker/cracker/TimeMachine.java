package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.population.PopulationData;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;
import net.minecraft.world.gen.ChunkRandom;

import java.util.List;

public class TimeMachine {
    
    private LCG inverseLCG = Rand.JAVA_LCG.combine(-2);

    public TimeMachine() {

    }

    public List<Long> buildStructureSeeds(int pillarSeed, List<StructureData> structureDataList, List<PopulationData> populationDataList, List<Long> structureSeeds) {
        ChunkRandom chunkRandom = new ChunkRandom();

        for(long i = 0; i < (1L << 32); i++) {
            if((i & ((1L << 28) - 1)) == 0) {
                SeedCracker.LOG.warn("Progress " + (i * 100.0f) / (1L << 32) + "%...");
            }

            long structureSeed = this.timeMachine(i, pillarSeed);
            boolean goodSeed = true;

            for(StructureData structureData: structureDataList) {
                if(!goodSeed)break;
                chunkRandom.setStructureSeed(structureSeed, structureData.getRegionX(),
                        structureData.getRegionZ(), structureData.getSalt());
                if(!structureData.test(chunkRandom))goodSeed = false;
            }

            for(PopulationData populationData: populationDataList) {
                if(!goodSeed)break;
                if(!populationData.test(structureSeed))goodSeed = false;
            }

            if(goodSeed) {
                structureSeeds.add(structureSeed);
            }
        }

        return structureSeeds;
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

}
