package com.github.legoatoom.enderbench.item;

import com.github.legoatoom.enderbench.EnderBench;
import com.github.legoatoom.enderbench.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

@SuppressWarnings("SameParameterValue")
public class ModItems {
    public static final Item ENDER_BENCH;

    private static Item registerBlockItem(String name, Block block, ItemGroup itemGroup){
        return Registry.register(Registry.ITEM, new Identifier(EnderBench.MODID, name), new BlockItem(block, new Item.Settings().group(itemGroup)));
    }

    static{
        ENDER_BENCH = registerBlockItem("ender_bench", ModBlocks.ENDER_BENCH, ItemGroup.DECORATIONS);
    }

    public static void load() {
        EnderBench.LOGGER.log(Level.INFO, "Loading Items");
    }
}
