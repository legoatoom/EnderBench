/*
 * Copyright (C) 2020  legoatoom
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.legoatoom.enderbench.mixin.client.network;

import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;
import com.github.legoatoom.enderbench.client.network.IClientPlayerEntity;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
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
