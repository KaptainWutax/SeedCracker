package kaptainwutax.seedcracker;

import com.mojang.blaze3d.platform.GlStateManager;
import kaptainwutax.seedcracker.finder.*;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    private List<Finder> activeFinders = new ArrayList<>();


    public FinderQueue() {

    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(World world, ChunkPos chunkPos) {
        BuriedTreasureFinder buriedTreasure = new BuriedTreasureFinder(world, chunkPos);
        buriedTreasure.findInChunk();

        DungeonFinder dungeonFinder = new DungeonFinder(world, chunkPos);
        dungeonFinder.findInChunk();

        SwampHutFinder swampHutFinder = new SwampHutFinder(world, chunkPos);
        swampHutFinder.findInChunk();

        DesertTempleFinder desertTempleFinder = new DesertTempleFinder(world, chunkPos);
        desertTempleFinder.findInChunk();

        JungleTempleFinder jungleTempleFinder = new JungleTempleFinder(world, chunkPos);
        jungleTempleFinder.findInChunk();

        this.activeFinders.add(buriedTreasure);
        this.activeFinders.add(dungeonFinder);
        this.activeFinders.add(swampHutFinder);
        this.activeFinders.add(desertTempleFinder);
        this.activeFinders.add(jungleTempleFinder);
    }

    public void renderFinders() {
        GlStateManager.disableTexture();

        //Makes it render through blocks.
        GlStateManager.disableDepthTest();

        this.activeFinders.removeIf(Finder::isUseless);

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
