package com.github.legoatoom.enderbench.client.network;

import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ModClientPacketRegister {

    public static void init(){
        ClientSidePacketRegistry.INSTANCE.register(PacketIDs.S2C_SETCONNECTION_PACKET_ID,
                (packetContext, packetByteBuf) -> {
                    UUID uuid = packetByteBuf.readUuid();
                    BlockPos pos = packetByteBuf.readBlockPos();
                    EnderBenchEntity entity = (EnderBenchEntity) packetContext.getPlayer().world.getBlockEntity(pos);
                    if (entity == null) return;
                    packetContext.getTaskQueue().execute(() -> {
                        if (packetContext.getPlayer().getUuid().equals(uuid))
                            ((IClientPlayerEntity) packetContext.getPlayer()).enderbench_setConnectedBenchPos(pos);
                        entity.setMasterUuid(uuid);
                    });
                });
    }
}
