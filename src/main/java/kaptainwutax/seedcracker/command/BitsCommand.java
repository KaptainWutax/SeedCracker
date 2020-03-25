package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public class BitsCommand extends ClientCommand {

	@Override
	public String getName() {
		return "bits";
	}

	@Override
	public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.executes(this::printBits);
	}

	private int printBits(CommandContext<ServerCommandSource> context) {
		DataStorage s = SeedCracker.get().getDataStorage();
		String message = "You current have collected " + (int)s.getBaseBits() + " bits out of " + (int)s.getWantedBits() + ".";
		this.sendFeedback(message, Formatting.GREEN, false);
		return 0;
	}

}
