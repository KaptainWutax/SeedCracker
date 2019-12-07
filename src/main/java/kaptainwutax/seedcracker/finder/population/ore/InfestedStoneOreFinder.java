package kaptainwutax.seedcracker.finder.population.ore;

import kaptainwutax.seedcracker.finder.Finder;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.ArrayList;
import java.util.List;

public class InfestedStoneOreFinder extends SimpleOreFinder {

    public static OreFeatureConfig CONFIG = new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.INFESTED_STONE.getDefaultState(), 9);

    protected static List<BlockPos> SEARCH_POSITIONS = Finder.buildSearchPositions(Finder.CHUNK_POSITIONS, pos -> {
        if(pos.getY() > 64 + 4)return true;
        return false;
    });

    public InfestedStoneOreFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, CONFIG);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public Vector4f getRenderColor() {
        return new Vector4f(0.5f, 0.5f, 0.5f, 1.0f);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new InfestedStoneOreFinder(world, chunkPos));
        finders.add(new InfestedStoneOreFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new InfestedStoneOreFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new InfestedStoneOreFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));
        return finders;
    }

}
