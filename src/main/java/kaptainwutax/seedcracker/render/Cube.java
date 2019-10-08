package kaptainwutax.seedcracker.render;

import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;

public class Cube extends Renderer {

    public BlockPos pos;
    public Vector4f color;

    private Line[] edges = new Line[12];

    public Cube() {
        this(BlockPos.ORIGIN, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public Cube(BlockPos pos) {
        this(pos, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public Cube(BlockPos pos, Vector4f color) {
        this.pos = pos;
        this.color = color;
        this.edges[0] = new Line(toVec3d(pos), toVec3d(pos.add(1, 0, 0)), this.color);
        this.edges[1] = new Line(toVec3d(pos), toVec3d(pos.add(0, 1, 0)), this.color);
        this.edges[2] = new Line(toVec3d(pos), toVec3d(pos.add(0, 0, 1)), this.color);
        this.edges[3] = new Line(toVec3d(pos.add(1, 0, 1)), toVec3d(pos.add(1, 0, 0)), this.color);
        this.edges[4] = new Line(toVec3d(pos.add(1, 0, 1)), toVec3d(pos.add(1, 1, 1)), this.color);
        this.edges[5] = new Line(toVec3d(pos.add(1, 0, 1)), toVec3d(pos.add(0, 0, 1)), this.color);
        this.edges[6] = new Line(toVec3d(pos.add(1, 1, 0)), toVec3d(pos.add(1, 0, 0)), this.color);
        this.edges[7] = new Line(toVec3d(pos.add(1, 1, 0)), toVec3d(pos.add(0, 1, 0)), this.color);
        this.edges[8] = new Line(toVec3d(pos.add(1, 1, 0)), toVec3d(pos.add(1, 1, 1)), this.color);
        this.edges[9] = new Line(toVec3d(pos.add(0, 1, 1)), toVec3d(pos.add(0, 0, 1)), this.color);
        this.edges[10] = new Line(toVec3d(pos.add(0, 1, 1)), toVec3d(pos.add(0, 1, 0)), this.color);
        this.edges[11] = new Line(toVec3d(pos.add(0, 1, 1)), toVec3d(pos.add(1, 1, 1)), this.color);
    }

    @Override
    public void render() {
        if(this.pos == null || this.color == null || this.edges == null)return;

        for(Line edge: this.edges) {
            if(edge == null)continue;
            edge.render();
        }
    }

}
