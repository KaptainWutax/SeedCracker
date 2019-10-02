package kaptainwutax.seedcracker.feature.decoration;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class DecorationFeature {

    protected World world;
    protected BlockPos pos;

    public DecorationFeature(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public abstract void reverseSeed();

    public Block getBlockAt(BlockPos pos) {
        return this.world.getBlockState(pos).getBlock();
    }

}
