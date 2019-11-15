package kaptainwutax.seedcracker.mixin;

import kaptainwutax.seedcracker.FinderQueue;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.ChunkDataS2CPacket;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;

    @Inject(method = "onChunkData", at = @At("RETURN"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        int chunkX = packet.getX();
        int chunkZ = packet.getZ();
        FinderQueue.get().onChunkData(this.world, new ChunkPos(chunkX, chunkZ));
    }

}
