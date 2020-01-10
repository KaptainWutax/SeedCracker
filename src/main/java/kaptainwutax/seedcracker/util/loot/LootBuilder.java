package kaptainwutax.seedcracker.util.loot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;

import java.io.File;

public class LootBuilder extends LootContext.Builder {

	private static final FakeWorld FAKE_WORLD = new FakeWorld();

	public LootBuilder() {
		super(FAKE_WORLD);
	}

	private static class FakeWorld extends ServerWorld {
		public FakeWorld() {
			super(new IntegratedServer(
					MinecraftClient.getInstance(), null, null,
					new LevelInfo(new LevelProperties(new CompoundTag(), null, 0, null)),
					null, null, null, null, null
			), Runnable::run, new WorldSaveHandler(new File("foo"), "bar", null, null),
					new LevelProperties(new CompoundTag(), null, 0, null),
					DimensionType.OVERWORLD, null, new WorldGenerationProgressListener() {
				@Override
				public void start(ChunkPos var1) {

				}

				@Override
				public void setChunkStatus(ChunkPos var1, ChunkStatus var2) {

				}

				@Override
				public void stop() {

				}
			});
		}
	}

}
