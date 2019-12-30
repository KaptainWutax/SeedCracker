package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedcracker.util.Seeds;
import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.ChunkPos;

public class StructureData {

    private int chunkX;
    private int chunkZ;
    private int regionX;
    private int regionZ;
    private int offsetX;
    private int offsetZ;
    private FeatureType featureType;

    public StructureData(ChunkPos chunkPos, FeatureType featureType) {
        this.featureType = featureType;
        this.featureType.build(this, chunkPos);
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public int getRegionX() {
        return this.regionX;
    }

    public int getRegionZ() {
        return this.regionZ;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetZ() {
        return this.offsetZ;
    }

    public int getSalt() {
        return this.featureType.salt;
    }

    public FeatureType getFeatureType() {
        return this.featureType;
    }

    public boolean test(long structureSeed, Rand rand) {
        Seeds.setStructureSeed(rand, structureSeed, this.regionX, this.regionZ, this.getSalt());
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

    public abstract static class FeatureType {
        public final int salt;
        public final int distance;

        public FeatureType(int salt, int distance) {
            this.salt = salt;
            this.distance = distance;
        }

        public void build(StructureData data, ChunkPos chunkPos) {
            int chunkX = chunkPos.x;
            int chunkZ = chunkPos.z;

            data.chunkX = chunkX;
            data.chunkZ = chunkZ;

            chunkX = chunkX < 0 ? chunkX - this.distance + 1 : chunkX;
            chunkZ = chunkZ < 0 ? chunkZ - this.distance + 1 : chunkZ;

            //Pick out in which region the chunk is.
            int regionX = (chunkX / this.distance);
            int regionZ = (chunkZ / this.distance);

            data.regionX = regionX;
            data.regionZ = regionZ;

            regionX *= this.distance;
            regionZ *= this.distance;

            data.offsetX = chunkPos.x - regionX;
            data.offsetZ = chunkPos.z - regionZ;
        }

        public abstract boolean test(Rand rand, StructureData data, long structureSeed);
    }

    public static final FeatureType DESERT_PYRAMID = new FeatureType(14357617, 32) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return rand.nextInt(24) == data.getOffsetX() && rand.nextInt(24) == data.getOffsetZ();
        }
    };

    public static final FeatureType IGLOO = new FeatureType(14357618, 32) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return rand.nextInt(24) == data.getOffsetX() && rand.nextInt(24) == data.getOffsetZ();
        }
    };

    public static final FeatureType JUNGLE_TEMPLE = new FeatureType(14357619, 32) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return rand.nextInt(24) == data.getOffsetX() && rand.nextInt(24) == data.getOffsetZ();
        }
    };

    public static final FeatureType SWAMP_HUT = new FeatureType(14357620, 32) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return rand.nextInt(24) == data.getOffsetX() && rand.nextInt(24) == data.getOffsetZ();
        }
    };

    public static final FeatureType OCEAN_RUIN = new FeatureType(14357621, 16) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return rand.nextInt(8) == data.getOffsetX() && rand.nextInt(8) == data.getOffsetZ();
        }
    };

    public static final FeatureType SHIPWRECK = new FeatureType(165745295, 16) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return rand.nextInt(8) == data.getOffsetX() && rand.nextInt(8) == data.getOffsetZ();
        }
    };

    public static final FeatureType PILLAGER_OUTPOST = new FeatureType(165745296, 32) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            if(rand.nextInt(24) != data.getOffsetX() || rand.nextInt(24) != data.getOffsetZ())return false;
            
            int xo = data.getChunkX() >> 4;
            int zo = data.getChunkZ() >> 4;
            rand.setSeed((long)(xo ^ zo << 4) ^ structureSeed, true);
            rand.nextInt();
            return rand.nextInt(5) == 0;
        }
    };

    public static final FeatureType END_CITY = new FeatureType(10387313, 20) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return (rand.nextInt(9) + rand.nextInt(9)) / 2 == data.getOffsetX()
                    && (rand.nextInt(9) + rand.nextInt(9)) / 2 == data.getOffsetZ();
        }
    };

    public static final FeatureType OCEAN_MONUMENT = new FeatureType(10387313, 32) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return (rand.nextInt(27) + rand.nextInt(27)) / 2 == data.getOffsetX()
                    && (rand.nextInt(27) + rand.nextInt(27)) / 2 == data.getOffsetZ();
        }
    };

    public static final FeatureType BURIED_TREASURE = new FeatureType(10387320, 1) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return rand.nextFloat() < 0.01f;
        }
    };

    public static final FeatureType WOODLAND_MANSION = new FeatureType(10387319, 80) {
        @Override
        public boolean test(Rand rand, StructureData data, long structureSeed) {
            return (rand.nextInt(60) + rand.nextInt(60)) / 2 == data.getOffsetX()
                    && (rand.nextInt(60) + rand.nextInt(60)) / 2 == data.getOffsetZ();
        }
    };

}
