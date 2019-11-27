package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiamondOreFinder extends OreFinder {

    public static OreFeatureConfig CONFIG = new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.DIAMOND_ORE.getDefaultState(), 8);

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        if(pos.getY() > 16)return true;
        return false;
    });

    public DiamondOreFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, CONFIG);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public void findOreVeins(List<Set<BlockPos>> veins) {
        List<BlockPos> starts = new ArrayList<>();

        for(Set<BlockPos> vein: veins) {
            BlockPos oreStart = new BlockPos(
                this.findX(vein, OreFinder.HIGHEST),
                this.findY(vein, OreFinder.LOWEST) + 3,
                this.findZ(vein, OreFinder.HIGHEST)
            );

            starts.add(oreStart);
        }

        starts.forEach(start -> {
            this.renderers.add(new Cube(start, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)));
        });
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new DiamondOreFinder(world, chunkPos));
        finders.add(new DiamondOreFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new DiamondOreFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new DiamondOreFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));
        return finders;
    }

}
