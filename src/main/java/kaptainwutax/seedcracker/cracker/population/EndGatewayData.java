package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedutils.lcg.rand.JRand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;

public class EndGatewayData extends DecoratorData {

    private static final double BITS = Math.log(700 * 16 * 16 * 7) / Math.log(2);
    private static final int SALT = 30000;

    private int xOffset;
    private int zOffset;
    private int height;

    public EndGatewayData(ChunkPos chunkPos, Biome biome, BlockPos pos, int height) {
        super(chunkPos, SALT, biome);
        this.xOffset = pos.getX() & 15;
        this.zOffset = pos.getZ() & 15;
        this.height = height;
    }

    @Override
    public boolean testDecorator(JRand rand) {
        if(rand.nextInt(700) != 0)return false;
        if(rand.nextInt(16) != this.xOffset)return false;
        if(rand.nextInt(16) != this.zOffset)return false;
        if(rand.nextInt(7) != this.height - 3)return false;

        return true;
    }

    @Override
    public double getBits() {
        return BITS;
    }

    @Override
    public void onDataAdded(DataStorage dataStorage) {
        dataStorage.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
    }

}
