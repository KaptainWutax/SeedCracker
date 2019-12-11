package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.List;

public class DungeonData extends PopulationData {

    public static LCG REVERSE_SKIP = Rand.JAVA_LCG.combine(-1);
    public static LCG Y_START_SKIP = Rand.JAVA_LCG.combine(2);
    public static LCG Y_SKIP = Rand.JAVA_LCG.combine(5);

    public static final Integer COBBLESTONE_CALL = 0;
    public static final Integer MOSSY_COBBLESTONE_CALL = 1;

    private List<BlockPos> starts;
    private final List<List<Integer>> floorCallsList;

    public DungeonData(ChunkPos chunkPos, Biome biome, List<BlockPos> starts, List<List<Integer>> floorCallsList) {
        super(chunkPos, Decorator.DUNGEONS, biome);
        this.starts = starts;
        this.floorCallsList = floorCallsList;
    }

    @Override
    public boolean testDecorator(long decoratorSeed) {
        if(this.starts.isEmpty())return true;

        //TODO: This currently only supports 1 dungeon per chunk.
        BlockPos start = this.starts.get(0);

        long currentSeed = decoratorSeed;
        boolean valid = false;

        for(int i = 0; i < 8; i++) {
            currentSeed = i == 0 ? Y_START_SKIP.nextSeed(currentSeed) : Y_SKIP.nextSeed(currentSeed);

            if(currentSeed >> 40 == start.getY()) {
                valid = true;
                break;
            }
        }

        if(!valid)return false;

        int x = (int)(REVERSE_SKIP.nextSeed(currentSeed) >> 44);
        if(x != start.getX())return false;

        int z = (int)(Rand.JAVA_LCG.nextSeed(currentSeed) >> 44);
        if(z != start.getZ())return false;

        return true;
    }

}
