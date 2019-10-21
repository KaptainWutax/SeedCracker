package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.world.ChunkWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class PieceFinder extends Finder {

    protected Map<BlockPos, BlockState> structure = new HashMap<>();
    private MutableIntBoundingBox boundingBox;
    private final Predicate<BlockPos> searchPredicate;

    protected Direction facing;
    private BlockMirror mirror;
    private BlockRotation rotation;

    //SizeX, width here for clarity when comparing with original code.
    protected int width;

    //SizeY, width here for clarity when comparing with original code.
    protected int height;

    //SizeZ, width here for clarity when comparing with original code.
    protected int depth;

    public PieceFinder(World world, ChunkPos chunkPos, Direction facing, Vec3i size, Predicate<BlockPos> searchPredicate) {
        super(world, chunkPos);
        this.setOrientation(facing);

        this.width = size.getX();
        this.height = size.getY();
        this.depth = size.getZ();

        if(this.facing.getAxis() == Direction.Axis.Z) {
            this.boundingBox = new MutableIntBoundingBox(
                    0, 0, 0,
                    size.getX() - 1, size.getY() - 1, size.getZ() - 1
            );
        } else {
            this.boundingBox = new MutableIntBoundingBox(
                    0, 0, 0,
                    size.getZ() - 1, size.getY() - 1, size.getX() - 1
            );
        }

        this.searchPredicate = searchPredicate;
    }

    public Vec3i getLayout() {
        if(this.facing.getAxis() != Direction.Axis.Z) {
            return new Vec3i(this.depth, this.height, this.width);
        }

        return new Vec3i(this.width, this.height, this.depth);
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        ChunkWrapper chunkWrapper = new ChunkWrapper(this.world, this.chunkPos);
        Chunk chunk = chunkWrapper.getChunk();

        for(BlockPos center: CHUNK_POSITIONS) {
            if(!this.searchPredicate.test(center))continue;

            boolean found = true;

            for(Map.Entry<BlockPos, BlockState> entry: this.structure.entrySet()) {
                BlockPos pos = this.chunkPos.getCenterBlockPos().add(center.add(entry.getKey()));
                BlockState state = this.world.getBlockState(pos);

                //Blockstate may change when it gets placed in the world, that's why it's using the block here.
                if(!state.getBlock().equals(entry.getValue().getBlock())) {
                    found = false;
                    break;
                }
            }

            if(found) {
                result.add(this.chunkPos.getCenterBlockPos().add(center));
            }
        }

        return result;
    }

    public void setOrientation(Direction facing) {
        this.facing = facing;

        if(facing == null) {
            this.rotation = BlockRotation.NONE;
            this.mirror = BlockMirror.NONE;
        } else {
            switch(facing) {
                case SOUTH:
                    this.mirror = BlockMirror.LEFT_RIGHT;
                    this.rotation = BlockRotation.NONE;
                    break;
                case WEST:
                    this.mirror = BlockMirror.LEFT_RIGHT;
                    this.rotation = BlockRotation.CLOCKWISE_90;
                    break;
                case EAST:
                    this.mirror = BlockMirror.NONE;
                    this.rotation = BlockRotation.CLOCKWISE_90;
                    break;
                default:
                    this.mirror = BlockMirror.NONE;
                    this.rotation = BlockRotation.NONE;
            }
        }

    }

    protected int applyXTransform(int x, int z) {
        if (this.facing == null) {
            return x;
        } else {
            switch(this.facing) {
                case NORTH:
                case SOUTH:
                    return this.boundingBox.minX + x;
                case WEST:
                    return this.boundingBox.maxX - z;
                case EAST:
                    return this.boundingBox.minX + z;
                default:
                    return x;
            }
        }
    }

    protected int applyYTransform(int y) {
        return this.facing == null ? y : y + this.boundingBox.minY;
    }

    protected int applyZTransform(int x, int z) {
        if (this.facing == null) {
            return z;
        } else {
            switch(this.facing) {
                case NORTH:
                    return this.boundingBox.maxZ - z;
                case SOUTH:
                    return this.boundingBox.minZ + z;
                case WEST:
                case EAST:
                    return this.boundingBox.minZ + x;
                default:
                    return z;
            }
        }
    }

    protected BlockState getBlockAt(int ox, int oy, int oz) {
        int x = this.applyXTransform(ox, oz);
        int y = this.applyYTransform(oy);
        int z = this.applyZTransform(ox, oz);
        BlockPos pos = new BlockPos(x, y, z);

        return !this.boundingBox.contains(pos) ?
                Blocks.AIR.getDefaultState() :
                this.structure.getOrDefault(pos, Blocks.AIR.getDefaultState());
    }

    protected void fillWithOutline(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState outline, BlockState inside, boolean onlyReplaceAir) {
        for(int y = minY; y <= maxY; ++y) {
            for(int x = minX; x <= maxX; ++x) {
                for(int z = minZ; z <= maxZ; ++z) {
                    if(!onlyReplaceAir || !this.getBlockAt(x, y, z).isAir()) {
                        if(y != minY && y != maxY && x != minX && x != maxX && z != minZ && z != maxZ) {
                            this.addBlock(inside, x, y, z);
                        } else {
                            this.addBlock(outline, x, y, z);
                        }
                    }
                }
            }
        }

    }

    protected void addBlock(BlockState state, int x, int y, int z) {
        BlockPos pos = new BlockPos(
                this.applyXTransform(x, z),
                this.applyYTransform(y),
                this.applyZTransform(x, z)
        );

        if(this.boundingBox.contains(pos)) {
            if (this.mirror != BlockMirror.NONE) {
                state = state.mirror(this.mirror);
            }

            if (this.rotation != BlockRotation.NONE) {
                state = state.rotate(this.rotation);
            }

            this.structure.put(pos, state);
        }
    }

}
