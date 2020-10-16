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

package com.github.legoatoom.enderbench.client;

import com.github.legoatoom.enderbench.client.network.ModClientPacketRegister;
import com.github.legoatoom.enderbench.screen.ModScreenHandlerType;
import com.github.legoatoom.enderbench.screen.ingame.EnderBenchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class EnderBenchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ModScreenHandlerType.ENDER_BENCH_SCREEN_HANDLER_SCREEN_HANDLER, EnderBenchScreen::new);
        ModClientPacketRegister.init();
    }
}
