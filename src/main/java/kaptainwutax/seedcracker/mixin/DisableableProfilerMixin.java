package kaptainwutax.seedcracker.mixin;

import kaptainwutax.seedcracker.render.RenderTracker;
import net.minecraft.util.profiler.DisableableProfiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisableableProfiler.class)
public class DisableableProfilerMixin {

    @Inject(method = "swap(Ljava/lang/String;)V", at = @At("HEAD"))
    private void swap(String type, CallbackInfo ci) {
        RenderTracker.get().onRender(type);
    }

}
