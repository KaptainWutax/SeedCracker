package kaptainwutax.seedcracker.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.biome.GeneratorTypeData;
import kaptainwutax.seedcracker.init.ClientCommands;
import kaptainwutax.seedcracker.cracker.biome.HashedSeedData;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.util.Log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.ChunkDataS2CPacket;
import net.minecraft.client.network.packet.CommandTreeS2CPacket;
import net.minecraft.client.network.packet.GameJoinS2CPacket;
import net.minecraft.client.network.packet.PlayerRespawnS2CPacket;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;
    @Shadow private CommandDispatcher<CommandSource> commandDispatcher;

    @Inject(method = "onChunkData", at = @At(value = "TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
        int chunkX = packet.getX();
        int chunkZ = packet.getZ();
        FinderQueue.get().onChunkData(this.world, new ChunkPos(chunkX, chunkZ));
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(MinecraftClient mc, Screen screen, ClientConnection connection, GameProfile profile, CallbackInfo ci) {
        ClientCommands.registerCommands((CommandDispatcher<ServerCommandSource>)(Object)this.commandDispatcher);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "onCommandTree", at = @At("TAIL"))
    public void onOnCommandTree(CommandTreeS2CPacket packet, CallbackInfo ci) {
        ClientCommands.registerCommands((CommandDispatcher<ServerCommandSource>)(Object)this.commandDispatcher);
    }

    @Inject(method = "onGameJoin", at = @At(value = "TAIL"))
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        GeneratorTypeData generatorTypeData = new GeneratorTypeData(packet.getGeneratorType());

        Log.warn("Fetched the generator type [" +
                I18n.translate(generatorTypeData.getGeneratorType().getTranslationKey()).toUpperCase() + "].");

        if(!SeedCracker.get().getDataStorage().addGeneratorTypeData(generatorTypeData)) {
            Log.error("THIS GENERATOR IS NOT SUPPORTED!");
            Log.error("Overworld biome search WILL NOT run.");
        }
    }

    @Inject(method = "onPlayerRespawn", at = @At(value = "TAIL"))
    public void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        HashedSeedData hashedSeedData = new HashedSeedData(packet.method_22425());

        if(SeedCracker.get().getDataStorage().addHashedSeedData(hashedSeedData)) {
            Log.warn("Fetched hashed world seed [" + hashedSeedData.getHashedSeed() + "].");
        }
    }

}
