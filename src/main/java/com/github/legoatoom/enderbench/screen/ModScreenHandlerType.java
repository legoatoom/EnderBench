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

import com.github.legoatoom.enderbench.EnderBench;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

public class ModScreenHandlerType {
    public static final ScreenHandlerType<EnderBenchScreenHandler> ENDER_BENCH_SCREEN_HANDLER_SCREEN_HANDLER;

    static{
        ENDER_BENCH_SCREEN_HANDLER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(
                new Identifier(EnderBench.MODID, "ender_bench_screen_handler"), EnderBenchScreenHandler::new);

    }

    public static void load(){
        EnderBench.LOGGER.log(Level.INFO, "Loading ScreenHandlers");
    }
}
