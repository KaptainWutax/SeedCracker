package kaptainwutax.seedcracker.util;

import kaptainwutax.seedcracker.finder.Finder;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;

@FunctionalInterface
public interface FinderBuilder {

    List<Finder> build(World world, ChunkPos chunkPos);

}
