package kaptainwutax.seedcracker.cracker.structure;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.SeedData;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.cracker.structure.type.FeatureType;
import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.seed.ChunkSeeds;
import net.minecraft.util.math.ChunkPos;

public class StructureData extends SeedData {

    public int chunkX;
    public int chunkZ;
    public int regionX;
    public int regionZ;
    public int offsetX;
    public int offsetZ;
    private long baseRegionSeed;
    private FeatureType<StructureData> featureType;

    public StructureData(ChunkPos chunkPos, FeatureType<StructureData> featureType) {
        this.featureType = featureType;
        this.featureType.build(this, chunkPos);
        this.baseRegionSeed = ChunkSeeds.getRegionSeed(0L, this.regionX, this.regionZ, this.featureType.salt, MCVersion.v1_15);
    }

    @Override
    public boolean test(long structureSeed, JRand rand) {
        rand.setSeed(this.baseRegionSeed + structureSeed, true);
        return this.featureType.test(rand, this, structureSeed);
    }

    @Override
    public double getBits() {
        return this.featureType.getBits();
    }

    @Override
    public void onDataAdded(DataStorage dataStorage) {
        dataStorage.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof StructureData) {
            StructureData structureData = ((StructureData)obj);
            return structureData.featureType == this.featureType && structureData.regionX == this.regionX && structureData.regionZ == this.regionZ;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.regionX * 961 +  this.regionZ * 31 + this.featureType.salt;
    }

}
