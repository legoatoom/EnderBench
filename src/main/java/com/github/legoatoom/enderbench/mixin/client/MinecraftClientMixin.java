package com.github.legoatoom.enderbench.mixin.client;

import com.github.legoatoom.enderbench.ModConfigs;
import com.github.legoatoom.enderbench.client.network.IClientPlayerEntity;
import com.github.legoatoom.enderbench.screen.ingame.EnderBenchScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    @Shadow @Nullable public ClientWorld world;

    @Shadow public abstract void openScreen(@Nullable Screen screen);

    @Redirect(
            method = "handleInputEvents()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/network/ClientPlayerEntity;openRidingInventory()V"),
                    to =  @At(value = "INVOKE",
                            target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;getAdvancementHandler()Lnet/minecraft/client/network/ClientAdvancementManager;")
            )
    )
    private void injected(MinecraftClient minecraftClient, @Nullable Screen screen){
        IClientPlayerEntity p = (IClientPlayerEntity)player;
        assert p != null;
        if (p.hasConnection() && player.isSneaking()){
            BlockPos pos = p.getConnectedBench().getPos();
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBlockPos(pos);
            ClientSidePacketRegistry.INSTANCE.sendToServer(ModConfigs.OPEN_BENCH_PACKET_ID,passedData);
        } else {
            this.openScreen(new InventoryScreen(player));
        }

    }
}
