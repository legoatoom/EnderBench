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

import com.github.legoatoom.enderbench.EnderBench;
import com.github.legoatoom.enderbench.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

@SuppressWarnings("SameParameterValue")
public class ModBlockEntityType {
    public static final BlockEntityType<EnderBenchEntity> ENDER_BENCH_ENTITY;

    static{
        ENDER_BENCH_ENTITY = registerBlockEntity("ender_bench_entity");
    }

    private static BlockEntityType<EnderBenchEntity> registerBlockEntity(String name) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(EnderBench.MODID, name),
                BlockEntityType.Builder.create(EnderBenchEntity::new, ModBlocks.ENDER_BENCH).build(null));
    }

    public static void load() {
        EnderBench.LOGGER.log(Level.INFO, "Loading Block Entities");
    }
}
