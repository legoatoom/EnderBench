package com.github.legoatoom.enderbench.block.entity;

import com.github.legoatoom.enderbench.ModConfigs;
import com.github.legoatoom.enderbench.screen.EnderBenchScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class EnderBenchEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {

    public static final int invSize = ModConfigs.EnderBenchSize;
    public static final int range = 32;
    private DefaultedList<ItemStack> inventory;

    public EnderBenchEntity() {
        super(ModBlockEntityType.ENDER_BENCH_ENTITY);
        this.inventory = DefaultedList.ofSize(invSize, ItemStack.EMPTY);
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
}
