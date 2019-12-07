package kaptainwutax.seedcracker.finder;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    public RenderType renderType = RenderType.XRAY;
    public FinderConfig finderConfig = new DefaultFinderConfig();

    private FinderQueue() {
        this.clear();
    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(World world, ChunkPos chunkPos) {
        this.finderConfig.getActiveFinderTypes().forEach(type -> {
           SERVICE.submit(() -> {
                List<Finder> finders = type.finderBuilder.build(world, chunkPos);

                finders.forEach(finder -> {
                    if(finder.isValidDimension(world.dimension.getType())) {
                        this.finderConfig.addFinder(type, finder);
                        finder.findInChunk();
                        this.finderConfig.addFinder(type, finder);
                    }
                });
            });
        });
    }

    public void renderFinders() {
        if(this.renderType == RenderType.OFF)return;

        GlStateManager.disableTexture();

        //Makes it render through blocks.
        if(this.renderType == RenderType.XRAY) {
            GlStateManager.disableDepthTest();
        }

        this.finderConfig.getActiveFinders().forEach(finder -> {
            if(finder.shouldRender()) {
                finder.render();
            }
        });

        GlStateManager.enableTexture();

        if(this.renderType == RenderType.XRAY) {
            GlStateManager.enableDepthTest();
        }
    }

    public void clear() {
        this.renderType = RenderType.XRAY;
        this.finderConfig = new DefaultFinderConfig();
    }

    public enum RenderType {
        OFF, ON, XRAY
    }

}
