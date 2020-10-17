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

package com.github.legoatoom.enderbench.block.entity;

import com.github.legoatoom.enderbench.ModConfigs;
import com.github.legoatoom.enderbench.block.EnderBenchBlock;
import com.github.legoatoom.enderbench.client.network.IClientPlayerEntity;
import com.github.legoatoom.enderbench.client.network.PacketIDs;
import com.github.legoatoom.enderbench.screen.EnderBenchScreenHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class EnderBenchEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory, Tickable, BlockEntityClientSerializable {

    public static final int invSize = ModConfigs.EnderBenchSize;
    public static final double range = ModConfigs.EnderBenchRange;

    public static final TargetPredicate IN_RANGE_PREDICATE = new TargetPredicate().setBaseMaxDistance(range);

    public boolean isLocked;

    private UUID masterUuid;
    private DefaultedList<ItemStack> inventory;

    public EnderBenchEntity() {
        super(ModBlockEntityType.ENDER_BENCH_ENTITY);
        this.inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);
    }

    public UUID getMasterUuid() {
        return masterUuid;
    }

    public DefaultedList<ItemStack> getItems(){
        return inventory;
    }

    @Override
    public int size() {
        return getItems().size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(getItems(), slot, amount);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()){
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new EnderBenchScreenHandler(syncId, inv, this);
    }

    @Override
    public void clear() {
        getItems().clear();
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state,tag);
        this.isLocked = tag.getBoolean("isLocked");
        if (tag.contains("connection")) this.masterUuid = tag.getUuid("connection");
        inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);
        Inventories.fromTag(tag,this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putBoolean("isLocked", isLocked);
        if (masterUuid != null) tag.putUuid("connection", masterUuid);
        Inventories.toTag(tag, this.inventory);
        return tag;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        if (tag.contains("connection")) this.masterUuid = tag.getUuid("connection");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        if (masterUuid != null) tag.putUuid("connection", masterUuid);
        return tag;
    }

    @Override
    public void tick() {
        if (this.world == null) return;
        boolean active = false;
        isLocked = world.getBlockState(this.getPos()).get(EnderBenchBlock.LOCKED);
        if (!isLocked && this.world.isClient()){
            masterUuid = null;
            // If it is not locked then everyone in range should be able to open it.
            // So this can be done in the client.
            List<? extends PlayerEntity> players = this.world.getPlayers();
            for (PlayerEntity player : players) {
                if (player instanceof ClientPlayerEntity && isPlayerInRange(player)){
                    IClientPlayerEntity p = (IClientPlayerEntity) player;
                    if (p.enderbench_getConnectedBenchPos() != null){
                        BlockPos original = p.enderbench_getConnectedBenchPos();
                        if (!this.pos.equals(original)){
                            double distance1 = player.squaredDistanceTo(getPos().getX(), getPos().getY(), getPos().getZ());
                            double distance2 = player.squaredDistanceTo(original.getX(), original.getY(), original.getZ());
                            if (distance1 < distance2){
                                p.enderbench_setConnectedBenchPos(this.pos);
                                active = true;
                            }
                        } else {
                            active = true;
                        }
                    } else {
                        p.enderbench_setConnectedBenchPos(this.pos);
                        active = true;
                    }
                }
            }
            BlockState state = this.getCachedState();
            world.setBlockState(pos, state.with(EnderBenchBlock.ACTIVE, active));
        } else if (!this.world.isClient()) {
            // If it is locked then only the currently closes player can access it.
            if (isLocked) {
                if (masterUuid == null) {
                    PlayerEntity playerEntity = this.world.getClosestPlayer((double) this.pos.getX() + 0.5D,
                            (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D, range, false);
                    if (playerEntity != null) {
                        active = true;
                        Stream<PlayerEntity> stream = PlayerStream.around(this.world, this.pos, range * range);
                        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                        masterUuid = playerEntity.getUuid();
                        passedData.writeUuid(playerEntity.getUuid());
                        passedData.writeBlockPos(this.pos);

                        stream.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player,
                                PacketIDs.S2C_SETCONNECTION_PACKET_ID, passedData));
                    }
                } else {
                    active = true;
                }
            } else {
                masterUuid = null;
            }
            BlockState state = this.getCachedState();
            world.setBlockState(pos, state.with(EnderBenchBlock.ACTIVE, active));
        } else if (isLocked && this.world.isClient){
            if (masterUuid != null) {
                IClientPlayerEntity a = ((IClientPlayerEntity) (world.getPlayerByUuid(masterUuid)));
                if (a != null) {
                    if (a.enderbench_getConnectedBenchPos() == null) {
                        a.enderbench_setConnectedBenchPos(this.pos);
                    }
                }
            }
        }

    }


    @Environment(EnvType.CLIENT)
    public void setMasterUuid(UUID masterUuid) {
        this.masterUuid = masterUuid;
    }

    public boolean isPlayerInRange(PlayerEntity playerEntity){
        double distance = playerEntity.squaredDistanceTo(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        return distance <= range * range;
    }


}
