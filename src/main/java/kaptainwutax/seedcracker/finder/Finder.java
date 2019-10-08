package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.render.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class Finder {

    protected static final List<BlockPos> CHUNK_POSITIONS = new ArrayList<>();
    protected static final List<BlockPos> SUB_CHUNK_POSITIONS = new ArrayList<>();

    protected MinecraftClient mc = MinecraftClient.getInstance();
    protected List<Renderer> renderers = new ArrayList<>();
    protected World world;
    protected ChunkPos chunkPos;

    static {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                for(int y = 0; y < 256; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if(y < 16)SUB_CHUNK_POSITIONS.add(pos);
                    CHUNK_POSITIONS.add(pos);
                }
            }
        }
    }

    public Finder(World world, ChunkPos chunkPos) {
        this.world = world;
        this.chunkPos = chunkPos;
    }

    public abstract List<BlockPos> findInChunk();

    public boolean shouldRender() {
        Vec3d playerPos = mc.player.getPos();

        double distance = playerPos.squaredDistanceTo(
                this.chunkPos.x * 16,
                playerPos.y,
                this.chunkPos.z * 16
        );

        int renderDistance = mc.options.viewDistance * 16 + 16;
        return distance <= renderDistance * renderDistance + 32;
    }

    public void render() {
        this.renderers.forEach(Renderer::render);
    }

    public boolean isUseless() {
        return this.renderers.isEmpty();
    }

}
