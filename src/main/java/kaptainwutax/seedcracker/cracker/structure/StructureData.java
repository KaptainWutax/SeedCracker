package kaptainwutax.seedcracker.cracker.structure;

import kaptainwutax.seedcracker.cracker.structure.type.FeatureType;
import kaptainwutax.seedcracker.util.Seeds;
import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.ChunkPos;

public class StructureData {

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

    public boolean test(long structureSeed, Rand rand) {
        Seeds.setRegionSeed(rand, structureSeed, this.regionX, this.regionZ, this.salt);
        return this.featureType.test(rand, this, structureSeed);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof StructureData) {
            StructureData structureData = ((StructureData)obj);
            return structureData.regionX == this.regionX && structureData.regionZ == this.regionZ && structureData.featureType == this.featureType;
        }

        return false;
    }

}
