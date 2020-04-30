package kaptainwutax.seedcracker.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.TextFormat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.lwjgl.system.CallbackI;

import java.util.regex.Pattern;

public class Log {

    public static void debug(String message) {
        PlayerEntity player = getPlayer();

        if(player != null) {
            schedule(() -> player.addChatMessage(new LiteralText(message), false));
        }
    }

    public static void warn(String message) {
        PlayerEntity player = getPlayer();

        if(player != null) {
            schedule(() -> player.addChatMessage(new LiteralText(message).formatted(Formatting.GREEN), false));
        }
    }

    public static void error(String message) {
        PlayerEntity player = getPlayer();

        if(player != null) {
            schedule(() -> player.addChatMessage(new LiteralText(message).formatted(Formatting.RED), false));
        }
    }

    public static void printSeed(String message, long seedValue) {
        String[] data = message.split(Pattern.quote("${SEED}"));
        String seed = String.valueOf(seedValue);

        Text text = Texts.bracketed((new LiteralText(seed)).styled(style -> {
            style.setColor(Formatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, seed)).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click"))).setInsertion(seed);
        }));

        PlayerEntity player = getPlayer();

        if(player != null) {
            schedule(() -> player.addChatMessage(new LiteralText(data[0]).append(text).append(new LiteralText(data[1])), false));
        }
    }

    private static void schedule(Runnable runnable) {
        MinecraftClient.getInstance().execute(runnable);
    }

    private static PlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

}
