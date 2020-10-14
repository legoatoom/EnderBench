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
    private static final Item ENDER_BENCH;

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
