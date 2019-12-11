package kaptainwutax.seedcracker.cracker.population;

import kaptainwutax.seedcracker.util.Rand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.Decorator;

public class EndGatewayData extends PopulationData {

    private int xOffset;
    private int zOffset;
    private int height;

    public EndGatewayData(ChunkPos chunkPos, Biome biome, BlockPos pos, int height) {
        super(chunkPos, Decorator.END_GATEWAY, biome);
        this.xOffset = pos.getX() & 15;
        this.zOffset = pos.getZ() & 15;
        this.height = height;
    }

    @Override
    public boolean testDecorator(long decoratorSeed) {
        Rand rand = new Rand(decoratorSeed, false);

        if(rand.nextInt(700) != 0)return false;
        if(rand.nextInt(16) != this.xOffset)return false;
        if(rand.nextInt(16) != this.zOffset)return false;
        if(rand.nextInt(7) != this.height - 3)return false;

        return true;
    }

}
