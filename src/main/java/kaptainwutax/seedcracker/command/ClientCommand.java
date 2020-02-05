package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public abstract class ClientCommand {

    public abstract String getName();

    public abstract void build(LiteralArgumentBuilder<ServerCommandSource> builder);

    protected final void sendFeedback(String message, Formatting color, boolean overlay) {
        MinecraftClient.getInstance().player.addChatMessage(new LiteralText(message).formatted(color), overlay);
    }

    public final void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal(this.getName());
        this.build(builder);
        dispatcher.register(literal(ClientCommands.PREFIX).then(builder));
    }

}
