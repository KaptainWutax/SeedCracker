package kaptainwutax.seedcracker.render;

import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class Cube extends Cuboid {

    public Cube() {
        this(BlockPos.ORIGIN, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public Cube(BlockPos pos) {
        this(pos, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public Cube(BlockPos pos, Vector4f color) {
        super(pos, new Vec3i(1, 1, 1), color);
    }

    @Override
    public BlockPos getPos() {
        return this.start;
    }

}
