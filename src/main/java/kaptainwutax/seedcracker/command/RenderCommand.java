package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcracker.FinderQueue;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class RenderCommand extends ClientCommand {

    @Override
    public String getName() {
        return "render";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.then(literal("outlines")
                .executes(context -> this.printRenderMode())
                .then(literal("XRAY").executes(context -> this.setRenderMode(FinderQueue.RenderType.XRAY)))
                .then(literal("ON").executes(context -> this.setRenderMode(FinderQueue.RenderType.ON)))
                .then(literal("OFF").executes(context -> this.setRenderMode(FinderQueue.RenderType.OFF)))
        );
    }

    private int printRenderMode() {
        this.sendFeedback("Current render mode is set to [" + FinderQueue.get().renderType + "].", false);
        return 0;
    }

    private int setRenderMode(FinderQueue.RenderType renderType) {
        FinderQueue.get().renderType = renderType;
        this.sendFeedback("Set render mode to [" + FinderQueue.get().renderType + "].", false);
        return 0;
    }

}
