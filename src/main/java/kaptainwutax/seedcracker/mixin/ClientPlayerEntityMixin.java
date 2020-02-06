package kaptainwutax.seedcracker.mixin;

import com.mojang.brigadier.StringReader;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.gui.GuiScreen;
import kaptainwutax.seedcracker.init.ClientCommands;
import kaptainwutax.seedcracker.init.KeyBindings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        SeedCracker.get().getDataStorage().tick();

        if(MinecraftClient.getInstance().currentScreen == null && KeyBindings.OPEN_MENU.isPressed()) {
            MinecraftClient.getInstance().openScreen(new GuiScreen("Test"));
        }
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if(message.startsWith("/")) {
            StringReader reader = new StringReader(message);
            reader.skip();

            //Removing this kill the mixin. Son of a bitch.
            int cursor = reader.getCursor();
            reader.setCursor(cursor);

            if(ClientCommands.isClientSideCommand(message.substring(1).split(Pattern.quote(" ")))) {
                ClientCommands.executeCommand(reader);
                ci.cancel();
            }
        }
    }

}
