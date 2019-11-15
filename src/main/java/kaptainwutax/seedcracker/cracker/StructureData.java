package kaptainwutax.seedcracker.cracker;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.ChunkRandom;

public class StructureData {

    private int regionX;
    private int regionZ;
    private int offsetX;
    private int offsetZ;
    private Feature feature;

    public StructureData(ChunkPos chunkPos, Feature feature) {
        this.feature = feature;
        this.feature.build(this, chunkPos);
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
        return this.feature.salt;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public boolean test(ChunkRandom rand) {
        return this.feature.test(rand, this.offsetX, this.offsetZ);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)return true;

        if(obj instanceof StructureData) {
            StructureData structureData = ((StructureData)obj);
            return structureData.regionX == this.regionX && structureData.regionZ == this.regionZ && structureData.feature == this.feature;
        }

        return false;
    }

    public abstract static class Feature {
        public final int salt;
        public final int distance;

        public Feature(int salt, int distance) {
            this.salt = salt;
            this.distance = distance;
        }

        public void build(StructureData data, ChunkPos chunkPos) {
            int chunkX = chunkPos.x;
            int chunkZ = chunkPos.z;

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

        public abstract boolean test(ChunkRandom rand, int x, int z);
    }

    public static final Feature DESERT_PYRAMID = new Feature(14357617, 32) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextInt(24) == x && rand.nextInt(24) == z;
        }
    };

    public static final Feature IGLOO = new Feature(14357618, 32) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextInt(24) == x && rand.nextInt(24) == z;
        }
    };

    public static final Feature JUNGLE_TEMPLE = new Feature(14357619, 32) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextInt(24) == x && rand.nextInt(24) == z;
        }
    };

    public static final Feature SWAMP_HUT = new Feature(14357620, 32) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextInt(24) == x && rand.nextInt(24) == z;
        }
    };

    public static final Feature OCEAN_RUIN = new Feature(14357621, 16) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextInt(8) == x && rand.nextInt(8) == z;
        }
    };

    public static final Feature SHIPWRECK = new Feature(165745295, 16) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextInt(8) == x && rand.nextInt(8) == z;
        }
    };

    public static final Feature PILLAGER_OUTPOST = new Feature(165745296, 32) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextInt(24) == x && rand.nextInt(24) == z;
        }
    };

    public static final Feature END_CITY = new Feature(10387313, 20) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return (rand.nextInt(9) + rand.nextInt(9)) / 2 == x
                    && (rand.nextInt(9) + rand.nextInt(9)) / 2 == z;
        }
    };

    public static final Feature OCEAN_MONUMENT = new Feature(10387313, 32) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return (rand.nextInt(27) + rand.nextInt(27)) / 2 == x
                    && (rand.nextInt(27) + rand.nextInt(27)) / 2 == z;
        }
    };

    public static final Feature BURIED_TREASURE = new Feature(10387320, 1) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return rand.nextFloat() < 0.1f;
        }
    };

    public static final Feature WOODLAND_MANSION = new Feature(10387319, 80) {
        @Override
        public boolean test(ChunkRandom rand, int x, int z) {
            return (rand.nextInt(60) + rand.nextInt(60)) / 2 == x
                    && (rand.nextInt(60) + rand.nextInt(60)) / 2 == z;
        }
    };

}
