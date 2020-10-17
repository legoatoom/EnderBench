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

package com.github.legoatoom.enderbench.client.network;

import com.github.legoatoom.enderbench.EnderBench;
import com.github.legoatoom.enderbench.block.EnderBenchBlock;
import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;
import com.github.legoatoom.enderbench.sound.ModSoundEvents;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModServerPacketRegister {

    public static void init(){
        ServerSidePacketRegistry.INSTANCE.register(PacketIDs.OPEN_BENCH_PACKET_ID, ((packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            PlayerEntity player = packetContext.getPlayer();
            packetContext.getTaskQueue().execute(() -> {
                World world = player.world;
                BlockState state = world.getBlockState(pos);

                // Check if the person can actually open it.
                try {
                    EnderBenchEntity entity = (EnderBenchEntity) world.getBlockEntity(pos);
                    assert entity != null;
                    if (!entity.isPlayerInRange(player)){
                        EnderBench.LOGGER.warn(
                                String.format("%s is trying to open an Ender Bench that they cannot reach!",
                                        player.getDisplayName()));
                        return;
                    }
                } catch (ClassCastException | NullPointerException ex){
                    EnderBench.LOGGER.warn(
                            String.format("%s wanted to open an Ender Bench that no longer exists.",
                                    player.getDisplayName()));
                    return;
                }

                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                    EnderBenchBlock.playSound(world, ModSoundEvents.ENDER_BENCH_OPEN, player.getBlockPos());
                }
            });
        }));

        ServerSidePacketRegistry.INSTANCE.register(PacketIDs.CLOSE_BENCH_PACKET_ID,
                ((packetContext, packetByteBuf) -> packetContext.getTaskQueue().execute(()
                        -> EnderBenchBlock.playSound(packetContext.getPlayer().world,
                        ModSoundEvents.ENDER_BENCH_CLOSE, packetContext.getPlayer().getBlockPos()))));
    }
}
