package kaptainwutax.seedcracker.mixin;

import com.mojang.brigadier.StringReader;
import kaptainwutax.seedcracker.ClientCommands;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if(!message.startsWith("/"))return;

        StringReader reader = new StringReader(message);
        reader.skip();
        int cursor = reader.getCursor();
        reader.setCursor(cursor);

        if(ClientCommands.isClientSideCommand(message.substring(1).split(" "))) {
            ClientCommands.executeCommand(reader);
            ci.cancel();
        }
    }

}
