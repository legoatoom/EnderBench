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

package com.github.legoatoom.enderbench;

import com.github.legoatoom.enderbench.block.ModBlocks;
import com.github.legoatoom.enderbench.block.entity.ModBlockEntityType;
import com.github.legoatoom.enderbench.client.network.ServerSidePacketRegister;
import com.github.legoatoom.enderbench.item.ModItems;
import com.github.legoatoom.enderbench.screen.ModScreenHandlerType;
import com.github.legoatoom.enderbench.sound.ModSoundEvents;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EnderBench implements ModInitializer {

    public static final String MODID = "enderbench";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
        ModBlocks.load();
        ModItems.load();
        ModScreenHandlerType.load();
        ModBlockEntityType.load();
        ModSoundEvents.load();
        ServerSidePacketRegister.init();
    }

}
