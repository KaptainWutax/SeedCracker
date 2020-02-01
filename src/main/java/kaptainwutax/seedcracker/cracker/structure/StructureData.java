package kaptainwutax.seedcracker.cracker.structure;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.SeedData;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.cracker.structure.type.FeatureType;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.Seeds;
import net.minecraft.util.math.ChunkPos;

public class StructureData extends SeedData {

    public int chunkX;
    public int chunkZ;
    public int regionX;
    public int regionZ;
    public int offsetX;
    public int offsetZ;
    private final int salt;
    private FeatureType<StructureData> featureType;

    public StructureData(ChunkPos chunkPos, FeatureType<StructureData> featureType) {
        this.featureType = featureType;
        this.salt = this.featureType.salt;
        this.featureType.build(this, chunkPos);
    }

    @Override
    public boolean test(long structureSeed, Rand rand) {
        Seeds.setRegionSeed(rand, structureSeed, this.regionX, this.regionZ, this.salt);
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
