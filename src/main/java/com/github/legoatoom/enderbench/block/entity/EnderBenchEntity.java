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
import com.github.legoatoom.enderbench.screen.EnderBenchScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EnderBenchEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory, Tickable {

    public static final int invSize = ModConfigs.EnderBenchSize;
    public static final double range = ModConfigs.EnderBenchRange;

    private UUID connectionUUID;
    private DefaultedList<ItemStack> inventory;

    public EnderBenchEntity() {
        super(ModBlockEntityType.ENDER_BENCH_ENTITY);
        this.inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);
    }

    public UUID getConnectionUUID() {
        return connectionUUID;
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
        inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);
        Inventories.fromTag(tag,this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);
        return tag;
    }

    @Override
    public void tick() {
        if (this.world != null && this.world.isClient()) {
            PlayerEntity playerEntity = this.world.getClosestPlayer((double)this.pos.getX() + 0.5D, (double)this.pos.getY()  + 0.5D, (double)this.pos.getZ()  + 0.5D, range, false);
            if (connectionUUID != null){
                if (!world.getBlockState(this.getPos()).get(EnderBenchBlock.LOCKED)) {
                    PlayerEntity owner = this.world.getPlayerByUuid(connectionUUID);
                    if (owner != null && !isPlayerInRange(owner)) {
                        connectionUUID = null;
                        IClientPlayerEntity p = (IClientPlayerEntity) owner;
                        p.setConnectedBench(null);
                    } else if (playerEntity != null) {
                        connectionUUID = playerEntity.getUuid();
                        IClientPlayerEntity p = (IClientPlayerEntity) playerEntity;
                        p.setConnectedBench(this);
                    } else {
                        connectionUUID = null;
                    }
                }
            } else if (playerEntity != null){
                connectionUUID = playerEntity.getUuid();
                IClientPlayerEntity p = (IClientPlayerEntity)playerEntity;
                p.setConnectedBench(this);
            }
            BlockState state = this.getCachedState();
            world.setBlockState(pos, state.with(EnderBenchBlock.ACTIVE, connectionUUID != null));
        }
    }

    private void scheduleUpdate() {
        assert this.world != null;
        this.world.getBlockTickScheduler().schedule(this.getPos(), this.getCachedState().getBlock(), 5);
    }

    private boolean isPlayerInRange(PlayerEntity playerEntity){
        double distance = playerEntity.squaredDistanceTo(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        return distance <= range * range;
    }

}
