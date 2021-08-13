package kaptainwutax.seedcracker.finder;

import com.mojang.blaze3d.systems.RenderSystem;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.profile.FinderConfig;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    public RenderType renderType = RenderType.XRAY;
    public FinderConfig finderProfile = new FinderConfig();

    private FinderQueue() {
        this.clear();
    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(World world, ChunkPos chunkPos) {
        if(!SeedCracker.get().isActive())return;

        this.finderProfile.getActiveFinderTypes().forEach(type -> {
            SERVICE.submit(() -> {
               try {
                   List<Finder> finders = type.finderBuilder.build(world, chunkPos);

                   finders.forEach(finder -> {
                       if(finder.isValidDimension(world.getDimension())) {
                           finder.findInChunk();
                           this.finderProfile.addFinder(type, finder);
                       }
                   });
               } catch(Exception e) {
                   e.printStackTrace();
               }
            });
        });
    }

    public void renderFinders(MatrixStack matrices) {
        if(this.renderType == RenderType.OFF)return;

        MatrixStack matrixStack = RenderSystem.getModelViewStack();

        matrixStack.push();
        matrixStack.method_34425(matrices.peek().getModel());
        RenderSystem.applyModelViewMatrix();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();

        //Makes it render through blocks.
        if(this.renderType == RenderType.XRAY) {
            RenderSystem.disableDepthTest();
        }

        this.finderProfile.getActiveFinders().forEach(finder -> {
            if(finder.shouldRender()) {
                finder.render();
            }
        });

        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }

    public void clear() {
        this.renderType = RenderType.XRAY;
        this.finderProfile = new FinderConfig();
    }

    public enum RenderType {
        OFF, ON, XRAY
    }

}
