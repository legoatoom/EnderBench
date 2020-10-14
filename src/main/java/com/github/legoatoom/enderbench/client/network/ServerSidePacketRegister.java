package com.github.legoatoom.enderbench.client.network;

import com.github.legoatoom.enderbench.block.EnderBenchBlock;
import com.github.legoatoom.enderbench.sound.ModSoundEvents;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ServerSidePacketRegister {

    public static void init(){
        ServerSidePacketRegistry.INSTANCE.register(PacketIDs.OPEN_BENCH_PACKET_ID, ((packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                World world = packetContext.getPlayer().world;
                BlockState state = world.getBlockState(pos);
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    packetContext.getPlayer().openHandledScreen(screenHandlerFactory);
                }
                EnderBenchBlock.playSound(world, ModSoundEvents.ENDER_BENCH_OPEN, packetContext.getPlayer().getBlockPos());
            });
        }));

        ServerSidePacketRegistry.INSTANCE.register(PacketIDs.CLOSE_BENCH_PACKET_ID,
                ((packetContext, packetByteBuf) -> packetContext.getTaskQueue().execute(()
                        -> EnderBenchBlock.playSound(packetContext.getPlayer().world,
                        ModSoundEvents.ENDER_BENCH_CLOSE, packetContext.getPlayer().getBlockPos()))));
    }
}
