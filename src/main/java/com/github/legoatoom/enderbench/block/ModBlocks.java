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

package com.github.legoatoom.enderbench.block;


import com.github.legoatoom.enderbench.EnderBench;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

@SuppressWarnings("SameParameterValue")
public class ModBlocks {
    public static final Block ENDER_BENCH;

    private static Block registerBlock(String name, Block block){
        return Registry.register(Registry.BLOCK, new Identifier(EnderBench.MODID, name), block);
    }

    static{
        ENDER_BENCH = registerBlock("ender_bench", new EnderBenchBlock(FabricBlockSettings.of(Material.STONE).strength(2.5f)));

    }

    public static void load(){
        EnderBench.LOGGER.log(Level.INFO, "Loading Blocks");
    }
}
