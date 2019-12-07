package kaptainwutax.seedcracker.finder.population.ore;

import kaptainwutax.seedcracker.finder.population.OreFinder;
import kaptainwutax.seedcracker.render.Cuboid;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class SimpleOreFinder extends OreFinder {

    public SimpleOreFinder(World world, ChunkPos chunkPos, OreFeatureConfig oreFeatureConfig) {
        super(world, chunkPos, oreFeatureConfig);
    }

    @Override
    public void findOreVeins(List<Set<BlockPos>> veins) {
        List<BlockPos> starts = new ArrayList<>();

        for(Set<BlockPos> vein: veins) {
            //This is a semi-accurate estimate.
            BlockPos oreStart = new BlockPos(
                    this.findX(vein, OreFinder.HIGHEST),
                    this.findY(vein, OreFinder.LOWEST) + 2,
                    this.findZ(vein, OreFinder.HIGHEST)
            );

            starts.add(oreStart);
        }

        //If the start overlaps with another chunk, get rid of it.
        starts.removeIf(start -> {
            if((start.getX() & 15) == 0 || (start.getX() & 15) == 15)return true;
            if((start.getZ() & 15) == 0 || (start.getZ() & 15) == 15)return true;
            return false;
        });

        starts.forEach(start -> {
            this.renderers.add(new Cuboid(start.add(-1, -1, -1), start.add(1, 0, 1), this.getRenderColor()));
        });
    }

    public abstract Vector4f getRenderColor();

}
