package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class EndPillarsFinder extends Finder {

    protected BedrockMarkerFinder[] bedrockMarkers = {
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(12, 0, 39)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(-13, 0, 39)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(-34, 0, 24)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(-42, 0, -1)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(-34, 0, -25)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(-13, 0, -40)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(12, 0, -40)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(33, 0, -25)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(42, 0, 0)),
            new BedrockMarkerFinder(this.world, this.chunkPos, new BlockPos(33, 0, 24))
    };

    public EndPillarsFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        for(BedrockMarkerFinder bedrockMarker: this.bedrockMarkers) {
            result.addAll(bedrockMarker.findInChunk());
        }

        result.forEach(pos -> this.renderers.add(new Cube(pos, new Vector4f(0.5f, 0.0f, 0.5f, 1.0f))));

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

        protected boolean isInChunk = true;

        public BedrockMarkerFinder(World world, ChunkPos chunkPos, BlockPos xz) {
            super(world, chunkPos, Blocks.BEDROCK);

            int localX = xz.getX() & 15;
            int localZ = xz.getZ() & 15;

            if(chunkPos.getStartX() + localX != xz.getX() || chunkPos.getStartZ() + localZ != xz.getZ()) {
                this.isInChunk = false;
                return;
            }

            this.searchPositions.removeIf(pos -> {
                if(pos.getX() != localX)return true;
                if(pos.getZ() != localZ)return true;
                return false;
            });
        }

        @Override
        public List<BlockPos> findInChunk() {
            return this.isInChunk ? super.findInChunk() : new ArrayList<>();
        }

        @Override
        public boolean isValidDimension(DimensionType dimension) {
            return true;
        }

    }

}
