package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.magic.PopulationReversal;
import kaptainwutax.seedcracker.util.Log;
import kaptainwutax.seedutils.lcg.rand.JRand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import randomreverser.ReverserDevice;
import randomreverser.call.FilteredSkip;
import randomreverser.call.NextInt;
import randomreverser.util.LCG;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class DungeonData extends DecoratorData {

    private static final double BITS = Math.log(256 * 16 * 16 * 0.125D) / Math.log(2);
    public static final int COBBLESTONE_CALL = 0;
    public static final int MOSSY_COBBLESTONE_CALL = 1;
    public static final float MIN_FLOOR_BITS = 26.0F;
    public static final float MAX_FLOOR_BITS = 48.0F;
    public static final int SALT = 20003;

    private BlockPos start;
    private Vec3i size;
    private int[] floorCalls;
    private float bitsCount;

    public DungeonData(ChunkPos chunkPos, Biome biome, BlockPos pos, Vec3i size, int[] floorCalls) {
        super(chunkPos, SALT, biome);
        this.start = new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        this.size = size;
        this.floorCalls = floorCalls;

	    if(floorCalls != null) {
		    for(int call: floorCalls) {
			    bitsCount += call == DungeonData.COBBLESTONE_CALL ? 2.0F : 0.0F;
		    }
	    }
    }

    public BlockPos getStart() {
        return this.start;
    }

    public boolean usesFloor() {
    	return this.bitsCount >= MIN_FLOOR_BITS && this.bitsCount <= MAX_FLOOR_BITS;
    }

    @Override
    public boolean testDecorator(JRand rand) {
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
        if(this.floorCalls == null || !this.usesFloor())return;
        if(dataStorage.getTimeMachine().structureSeeds != null)return;

        Log.warn("Short-cutting to dungeons...");

        ReverserDevice device = new ReverserDevice();
        device.addCall(NextInt.withValue(16, this.start.getX()));
        device.addCall(NextInt.withValue(16, this.start.getZ()));
        device.addCall(NextInt.withValue(256, this.start.getY()));
        device.addCall(NextInt.consume(2, 2)); //Skip size.

        for(int call: this.floorCalls) {
            if(call == COBBLESTONE_CALL) {
                device.addCall(NextInt.withValue(4, 0));
            } else if(call == MOSSY_COBBLESTONE_CALL) {
                //Skip mossy, brute-force later.
                device.addCall(FilteredSkip.filter(r -> r.nextInt(4) != 0));
            }
        }

        Set<Long> decoratorSeeds = device.streamSeeds().sequential().limit(1).collect(Collectors.toSet());

        if(decoratorSeeds.isEmpty()) {
            Log.error("Finished dungeon search with no seeds.");
            return;
        }

        dataStorage.getTimeMachine().structureSeeds = new ArrayList<>();
        LCG failedDungeon = LCG.JAVA.combine(-5);

        for(long decoratorSeed: decoratorSeeds) {
            for(int i = 0; i < 8; i++) {
                PopulationReversal.getWorldSeeds((decoratorSeed ^ LCG.JAVA.multiplier) - SALT,
                        this.getChunkPos().x << 4, this.getChunkPos().z << 4).forEach(structureSeed -> {
                    Log.printSeed("Found structure seed ${SEED}.", structureSeed);
                    dataStorage.getTimeMachine().structureSeeds.add(structureSeed);
                });

                decoratorSeed = failedDungeon.nextSeed(decoratorSeed);
            }
        }

        dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
    }

}
