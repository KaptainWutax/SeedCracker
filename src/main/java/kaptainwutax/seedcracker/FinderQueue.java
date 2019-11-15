package kaptainwutax.seedcracker;

import com.mojang.blaze3d.platform.GlStateManager;
import io.netty.util.internal.ConcurrentSet;
import kaptainwutax.seedcracker.finder.*;
import kaptainwutax.seedcracker.util.FinderBuilder;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    public static final ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    private List<FinderBuilder> finderBuilders = new ArrayList<>();
    private Set<Finder> activeFinders = new ConcurrentSet<>();

    private FinderQueue() {
        this.clear();
    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(World world, ChunkPos chunkPos) {
        this.finderBuilders.forEach(finderBuilder -> {
           SERVICE.submit(() -> {
                List<Finder> finders = finderBuilder.build(world, chunkPos);

                finders.forEach(finder -> {
                    if (finder.isValidDimension(finder.getWorld().dimension.getType())) {
                        finder.findInChunk();
                        if(!finder.isUseless()) {
                            this.activeFinders.add(finder);
                        }
                    }
                });
            });
        });
    }

    public void renderFinders() {
        GlStateManager.disableTexture();

        //Makes it render through blocks.
        GlStateManager.disableDepthTest();

        this.activeFinders.forEach(finder -> {
            if(finder.shouldRender()) {
                finder.render();
            }
        });

        GlStateManager.enableTexture();
        GlStateManager.enableDepthTest();
    }

    public void clear() {
        this.activeFinders.clear();
        this.finderBuilders.clear();

        this.finderBuilders.add(DungeonFinder::create);
        this.finderBuilders.add(BuriedTreasureFinder::create);
        this.finderBuilders.add(SwampHutFinder::create);
        this.finderBuilders.add(DesertTempleFinder::create);
        this.finderBuilders.add(JungleTempleFinder::create);
        this.finderBuilders.add(EndPillarsFinder::create);
        this.finderBuilders.add(BiomeFinder::create);
        this.finderBuilders.add(OceanMonumentFinder::create);
        this.finderBuilders.add(EndCityFinder::create);
        this.finderBuilders.add(IglooFinder::create);
    }

}
