package com.github.legoatoom.enderbench.mixin.client.network;

import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
@Implements(@Interface(iface = IClientPlayerEntity.class, prefix = "ident$"))
public class ClientPlayerEntityMixin {

    public EnderBenchEntity enderBenchConnection;
}
