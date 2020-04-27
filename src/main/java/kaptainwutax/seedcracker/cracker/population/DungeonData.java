package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.magic.PopulationReversal;
import kaptainwutax.seedcracker.magic.RandomSeed;
import kaptainwutax.seedcracker.util.Log;
import kaptainwutax.seedcracker.util.Rand;
import kaptainwutax.seedcracker.util.math.LCG;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import randomreverser.RandomReverser;

import java.util.ArrayList;
import java.util.List;

public class DungeonData extends DecoratorData {

    private static final double BITS = Math.log(256 * 16 * 16 * 0.125D) / Math.log(2);
    public static final int COBBLESTONE_CALL = 0;
    public static final int MOSSY_COBBLESTONE_CALL = 1;
    public static final int SALT = 20003;

    private BlockPos start;
    private Vec3i size;
    private int[] floorCalls;
    private int bitsCount;

    public DungeonData(ChunkPos chunkPos, Biome biome, BlockPos pos, Vec3i size, int[] floorCalls, int bitsCount) {
        super(chunkPos, SALT, biome);
        this.start = new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ());
        this.size = size;
        this.floorCalls = floorCalls;
        this.bitsCount = bitsCount;
    }

    public BlockPos getStart() {
        return this.start;
    }

    @Override
    public boolean testDecorator(Rand rand) {
        for(int i = 0; i < 8; i++) {
            int x = rand.nextInt(16);
            int z = rand.nextInt(16);
            int y = rand.nextInt(256);

            if(y == this.start.getY() && x == this.start.getX() && z == this.start.getZ()) {
                return true;
            }

            rand.nextInt(2);
            rand.nextInt(2);
        }

        return false;
    }

    @Override
    public double getBits() {
        return BITS;
    }

    @Override
    public void onDataAdded(DataStorage dataStorage) {
        dataStorage.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
        if(this.floorCalls == null || this.bitsCount < 32)return;
        if(dataStorage.getTimeMachine().structureSeeds != null)return;

        Log.warn("Short-cutting to dungeons...");

        RandomReverser device = new RandomReverser();
        device.addNextIntCall(16, this.start.getX(), this.start.getX());
        device.addNextIntCall(16, this.start.getZ(), this.start.getZ());
        device.addNextIntCall(256, this.start.getY(), this.start.getY());
        device.consumeNextIntCalls(2); //Skip size.

        for(int call: this.floorCalls) {
            if(call == COBBLESTONE_CALL)device.addNextIntCall(4, 0, 0);
            else if(call == MOSSY_COBBLESTONE_CALL)device.consumeNextIntCalls(1); //Skip mossy.
        }

        ArrayList<Long> decoratorSeeds = device.findAllValidSeeds();

        if(decoratorSeeds.isEmpty()) {
            Log.error("Finished dungeon search with no seeds.");
            return;
        }

        dataStorage.getTimeMachine().structureSeeds = new ArrayList<>();
        LCG failedDungeon = Rand.JAVA_LCG.combine(-5);

        for(long decoratorSeed: decoratorSeeds) {
            for(int i = 0; i < 8; i++) {
                PopulationReversal.getWorldSeeds((decoratorSeed ^ Rand.JAVA_LCG.multiplier) - SALT,
                        this.getChunkPos().x << 4, this.getChunkPos().z << 4).forEach(structureSeed -> {
                    Log.debug("Found structure seed [" + structureSeed + "].");
                    dataStorage.getTimeMachine().structureSeeds.add(structureSeed);
                });

                decoratorSeed = failedDungeon.nextSeed(decoratorSeed);
            }
        }

        dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
    }

}
