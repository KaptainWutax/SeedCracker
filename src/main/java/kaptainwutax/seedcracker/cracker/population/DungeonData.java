package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.List;

public class DungeonData extends DecoratorData {

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
    public boolean testDecorator(Rand rand) {
        if(this.starts.isEmpty())return true;

        //TODO: This currently only supports 1 dungeon per chunk.
        BlockPos start = this.starts.get(0);

        for(int i = 0; i < 8; i++) {
            int x = rand.nextInt(16);
            int z = rand.nextInt(16);
            int y = rand.nextInt(256);

            if(y == start.getY() && x == start.getX() && z == start.getZ()) {
                return true;
            }

            rand.nextInt(2);
            rand.nextInt(2);
        }

        return false;
    }

}
