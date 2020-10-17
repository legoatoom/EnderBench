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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(AbstractClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity implements IClientPlayerEntity {

    private BlockPos enderBenchConnection;

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Override
    public boolean enderbench_hasConnection() {
        boolean hasConnection = true;
        try {
            BlockEntity entity = this.world.getBlockEntity(enderBenchConnection);
            if (entity instanceof EnderBenchEntity) {
                if (((EnderBenchEntity) entity).isLocked && (!this.getUuid().equals(((EnderBenchEntity) entity).getMasterUuid()))) {
                    hasConnection = false;
                    enderbench_setConnectedBenchPos(null);
                } else if (!((EnderBenchEntity) entity).isPlayerInRange(this)) {
                    hasConnection = false;
                    enderbench_setConnectedBenchPos(null);
                }
            } else {
                hasConnection = false;
                enderbench_setConnectedBenchPos(null);
            }
        } catch (NullPointerException e) { hasConnection = false; }
        return hasConnection;
    }

    @Override
    public BlockPos enderbench_getConnectedBenchPos() {
        return enderBenchConnection;
    }

    @Override
    public void enderbench_setConnectedBenchPos(@Nullable BlockPos enderBenchEntityPos) {
        enderBenchConnection = enderBenchEntityPos;
    }
}
