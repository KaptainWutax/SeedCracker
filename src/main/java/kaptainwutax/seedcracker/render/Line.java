package kaptainwutax.seedcracker.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Vec3d;

public class Line extends Renderer {

    public Vec3d start;
    public Vec3d end;
    public Vector4f color;

    public Line() {
        this(Vec3d.ZERO, Vec3d.ZERO, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public Line(Vec3d start, Vec3d end) {
        this(start, end, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public Line(Vec3d start, Vec3d end, Vector4f color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    @Override
    public void render() {
        if(this.start == null || this.end == null || this.color == null)return;

        Vec3d camPos = this.mc.gameRenderer.getCamera().getPos();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBufferBuilder();

        //This is how thick the line is.
        GlStateManager.lineWidth(2.0f);
        buffer.begin(3, VertexFormats.POSITION_COLOR);

        //Put the start and end vertices in the buffer.
        this.putVertex(buffer, camPos, this.start);
        this.putVertex(buffer, camPos, this.end);

        //Draw it all.
        tessellator.draw();
    }

    protected void putVertex(BufferBuilder buffer, Vec3d camPos, Vec3d pos) {
        for(int i = 0; i < 2; i++) {
            buffer.vertex(
                    pos.getX() - camPos.x,
                    pos.getY() - camPos.y,
                    pos.getZ() - camPos.z
            ).color(
                    this.color.getX(),
                    this.color.getY(),
                    this.color.getZ(),
                    this.color.getW()
            ).next();
        }
    }

}
