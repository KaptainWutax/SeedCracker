package kaptainwutax.seedcracker.mixin;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.finder.FinderQueue;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Inject(method = "disconnect", at = @At("TAIL"))
    private void disconnect(CallbackInfo ci) {
        SeedCracker.get().clear();
        FinderQueue.get().clear();
    }

}
