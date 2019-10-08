package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.render.RenderQueue;
import net.fabricmc.api.ModInitializer;

public class SeedCracker implements ModInitializer {

	@Override
	public void onInitialize() {
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
	}

}
