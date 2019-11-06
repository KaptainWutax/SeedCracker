package kaptainwutax.seedcracker;

import com.mojang.blaze3d.platform.GlStateManager;
import kaptainwutax.seedcracker.finder.*;
import kaptainwutax.seedcracker.util.FinderBuilder;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();

    private List<FinderBuilder> finderBuilders = new ArrayList<>();
    private List<Finder> activeFinders = new ArrayList<>();

    private FinderQueue() {
        this.finderBuilders.add(DungeonFinder::create);
        this.finderBuilders.add(BuriedTreasureFinder::create);
        this.finderBuilders.add(SwampHutFinder::create);
        this.finderBuilders.add(DesertTempleFinder::create);
        this.finderBuilders.add(JungleTempleFinder::create);
        this.finderBuilders.add(EndPillarsFinder::create);
    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(World world, ChunkPos chunkPos) {
        this.finderBuilders.forEach(finderBuilder -> {
            List<Finder> finders = finderBuilder.build(world, chunkPos);

            finders.forEach(finder -> {
                if(finder.isValidDimension(finder.getWorld().dimension.getType())) {
                    finder.findInChunk();
                    this.activeFinders.add(finder);
                }
            });
        });

        this.activeFinders.removeIf(Finder::isUseless);
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
    }

}
