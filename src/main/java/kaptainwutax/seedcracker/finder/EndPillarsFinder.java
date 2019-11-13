package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.PillarData;
import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EndPillarsFinder extends Finder {

    protected BedrockMarkerFinder[] bedrockMarkers = new BedrockMarkerFinder[10];

    public EndPillarsFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);

        for(int i = 0; i < this.bedrockMarkers.length; i++) {
            int x = MathHelper.floor(42.0D * Math.cos(2.0D * (-Math.PI + (Math.PI / 10.0D) * (double)i)));
            int z = MathHelper.floor(42.0D * Math.sin(2.0D * (-Math.PI + (Math.PI / 10.0D) * (double)i)));
            this.bedrockMarkers[i] = new BedrockMarkerFinder(this.world, new ChunkPos(new BlockPos(x, 0, z)), new BlockPos(x, 0, z));
        }
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        for(BedrockMarkerFinder bedrockMarker: this.bedrockMarkers) {
            result.addAll(bedrockMarker.findInChunk());
        }

        if(result.size() == this.bedrockMarkers.length) {
            result.forEach(pos -> this.renderers.add(new Cube(pos, new Vector4f(0.5f, 0.0f, 0.5f, 1.0f))));
            SeedCracker.get().onPillarData(new PillarData(result.stream().map(Vec3i::getY).collect(Collectors.toList())));
        }

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return dimension == DimensionType.THE_END;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EndPillarsFinder(world, chunkPos));
        return finders;
    }

    public class BedrockMarkerFinder extends BlockFinder {

        public BedrockMarkerFinder(World world, ChunkPos chunkPos, BlockPos xz) {
            super(world, chunkPos, Blocks.BEDROCK);

            int localX = xz.getX() & 15;
            int localZ = xz.getZ() & 15;

            this.searchPositions.removeIf(pos -> {
                if(pos.getX() != localX)return true;
                if(pos.getY() < 76)return true;
                if(pos.getY() > 76 + 3 * 10)return true;
                if(pos.getZ() != localZ)return true;
                return false;
            });
        }

        @Override
        public List<BlockPos> findInChunk() {
            return super.findInChunk();
        }

        @Override
        public boolean isValidDimension(DimensionType dimension) {
            return true;
        }

    }

}
