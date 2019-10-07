package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.finder.BlockFinder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;

public class SeedCracker implements ModInitializer {

	@Override
	public void onInitialize() {
		BlockFinder finder1 = new BlockFinder(Blocks.CHEST.getDefaultState());
		BlockFinder finder2 = new BlockFinder(Blocks.CHEST);
	}

}
