package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Cuboid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class PortalRoomFinder extends PieceFinder {

	protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
		if(pos.getY() > 63)return true;
		return false;
	});

	protected static Vec3i SIZE = new Vec3i(5, 1, 5);

	public PortalRoomFinder(World world, ChunkPos chunkPos) {
		super(world, chunkPos, Direction.NORTH, SIZE);
		this.searchPositions = SEARCH_POSITIONS;
		this.buildStructure();
	}

	@Override
	public List<BlockPos> findInChunk() {
		List<BlockPos> result = super.findInChunk();

		result.forEach(pos -> {
			this.renderers.add(new Cuboid(pos, SIZE, new Vector4f(0.1f, 0.4f, 0.0f, 1.0f)));
		});

		return result;
	}

	private void buildStructure() {
		BlockState northFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.NORTH);
		BlockState southFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.SOUTH);
		BlockState eastFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.EAST);
		BlockState westFrame = Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.WEST);
		this.addBlock(northFrame, 1, 0, 0);
		this.addBlock(northFrame, 2, 0, 0);
		this.addBlock(northFrame, 3, 0, 0);
		this.addBlock(southFrame, 1, 0, 4);
		this.addBlock(southFrame, 2, 0, 4);
		this.addBlock(southFrame, 3, 0, 4);
		this.addBlock(eastFrame, 0, 0, 1);
		this.addBlock(eastFrame, 0, 0, 2);
		this.addBlock(eastFrame, 0, 0, 3);
		this.addBlock(westFrame, 4, 0, 1);
		this.addBlock(westFrame, 4, 0, 2);
		this.addBlock(westFrame, 4, 0, 3);
	}

	@Override
	public boolean isValidDimension(DimensionType dimension) {
		return dimension == DimensionType.OVERWORLD;
	}

	public static List<Finder> create(World world, ChunkPos chunkPos) {
		List<Finder> finders = new ArrayList<>();
		finders.add(new PortalRoomFinder(world, chunkPos));

		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));

		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));

		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));
		finders.add(new PortalRoomFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)));
		return finders;
	}

}
