package kaptainwutax.seedcracker.util.loot;

import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;

public class LootBuilder extends LootContext.Builder {

	private static ServerWorld FAKE_WORLD;

	static {
		FAKE_WORLD = null;
	}

	public LootBuilder() {
		super(FAKE_WORLD);
	}

	//Best code I've ever written.
	/*private static class FakeWorld extends ServerWorld {
		public FakeWorld() throws IOException {
			super(new IntegratedServer(MinecraftClient.getInstance(),
							new LevelStorage(MinecraftClient.getInstance().runDirectory.toPath().resolve("foo"),
									MinecraftClient.getInstance().runDirectory.toPath().resolve("bar"), null).createSession("baz"),
							null, null, null, null, null), Runnable::run,
					new LevelStorage(MinecraftClient.getInstance().runDirectory.toPath().resolve("foo"),
							MinecraftClient.getInstance().runDirectory.toPath().resolve("bar"), null).createSession("baz"),
					new LevelProperties(null),
					null, new WorldGenerationProgressListener() {
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
	}*/

}
