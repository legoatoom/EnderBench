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

package com.github.legoatoom.enderbench.screen;

import com.github.legoatoom.enderbench.ModConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class EnderBenchScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    EnderBenchScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId, playerInventory, new SimpleInventory(ModConfigs.EnderBenchSize));
    }

    public EnderBenchScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory){
        super(ModScreenHandlerType.ENDER_BENCH_SCREEN_HANDLER_SCREEN_HANDLER, syncId);
        checkSize(inventory, ModConfigs.EnderBenchSize);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        int a,b;
        for (a=0; a < 3; a++){
            for (b = 0; b < 5; b++) {
                this.addSlot(new Slot(inventory, b + a * 5, 44 + b * 18, 17 + a * 18));
            }
        }
        for (a=0; a < 3; a++){
            for (b = 0; b < 9; b++) {
                this.addSlot(new Slot(playerInventory, b + a * 9 + 9, 8 + b * 18, 84 + a * 18));
            }
        }
        for (a=0; a < 9; a++){
            this.addSlot(new Slot(playerInventory, a, 8 + a * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }


}
