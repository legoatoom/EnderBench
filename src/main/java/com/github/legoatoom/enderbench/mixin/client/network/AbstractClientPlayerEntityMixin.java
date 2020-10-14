package com.github.legoatoom.enderbench.mixin.client.network;

import com.github.legoatoom.enderbench.ModConfigs;
import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;
import com.github.legoatoom.enderbench.client.network.IClientPlayerEntity;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements IClientPlayerEntity {

    private EnderBenchEntity enderBenchConnection;
    private boolean hasConnection = false;

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Override
    public boolean hasConnection() {
        if (hasConnection){
            if (enderBenchConnection.getConnectionUUID() == this.getUuid()){
                return true;
            } else {
                setConnectedBench(null);
            }
        }
        return false;
    }

    @Override
    public EnderBenchEntity getConnectedBench() {
        return enderBenchConnection;
    }

    @Override
    public void setConnectedBench(@Nullable EnderBenchEntity enderBenchEntity) {
        hasConnection = (enderBenchEntity != null);
        enderBenchConnection = enderBenchEntity;
    }
}
