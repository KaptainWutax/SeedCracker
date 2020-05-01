package kaptainwutax.seedcracker.util.loot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;

import java.io.File;
import java.io.IOException;

public class LootBuilder extends LootContext.Builder {

	private static FakeWorld FAKE_WORLD;

	static {
		try {
			FAKE_WORLD = new FakeWorld();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public LootBuilder() {
		super(FAKE_WORLD);
	}

	//Best code I've ever written.
	private static class FakeWorld extends ServerWorld {
		public FakeWorld() throws IOException {
			super(new IntegratedServer(MinecraftClient.getInstance(),
							new LevelStorage(MinecraftClient.getInstance().runDirectory.toPath().resolve("foo"),
									MinecraftClient.getInstance().runDirectory.toPath().resolve("bar"), null).createSession("baz"),
							null, null, null, null, null), Runnable::run,
					new LevelStorage(MinecraftClient.getInstance().runDirectory.toPath().resolve("foo"),
							MinecraftClient.getInstance().runDirectory.toPath().resolve("bar"), null).createSession("baz"),
					new LevelProperties(new CompoundTag(), null, 0, null),
					DimensionType.OVERWORLD, new WorldGenerationProgressListener() {
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
