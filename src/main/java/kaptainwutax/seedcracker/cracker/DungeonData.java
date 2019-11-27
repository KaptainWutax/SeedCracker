package kaptainwutax.seedcracker.cracker;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.ArrayList;
import java.util.List;

public class DungeonData extends PopulationData {

    public static final Feature DUNGEON = new Feature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, Decorator.DUNGEONS) {};
    public static final Integer COBBLESTONE_CALL = 0;
    public static final Integer MOSSY_COBBLESTONE_CALL = 1;

    private List<BlockPos> starts;
    private final List<List<Integer>> floorCallsList;

    public DungeonData(ChunkPos chunkPos, Biome biome, List<BlockPos> starts, List<List<Integer>> floorCallsList) {
        super(chunkPos, DUNGEON, biome);
        this.starts = starts;
        this.floorCallsList = floorCallsList;
    }

    @Override
    public boolean test(ChunkRandom chunkRandom) {
        List<Integer> idsToCheck = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            BlockPos pos = new BlockPos(chunkRandom.nextInt(16), chunkRandom.nextInt(256), chunkRandom.nextInt(16));
            int a = this.starts.indexOf(pos);
            if(a != -1)idsToCheck.add(a);

            //We assume the dungeon failed, skip 2 calls.
            chunkRandom.nextBoolean();
            chunkRandom.nextBoolean();
        }

        if(idsToCheck.size() != this.starts.size())return false;

        //TODO: Implement floor calls.

        return true;
    }

}
