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

package com.github.legoatoom.enderbench.sound;

import com.github.legoatoom.enderbench.EnderBench;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

public class ModSoundEvents {
    public static final SoundEvent ENDER_BENCH_OPEN;
    public static final SoundEvent ENDER_BENCH_CLOSE;


    private static SoundEvent registerSoundEvent(String name){
        Identifier identifier = new Identifier(EnderBench.MODID, name);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }

    static {
        ENDER_BENCH_OPEN = registerSoundEvent("ender_bench_open");
        ENDER_BENCH_CLOSE = registerSoundEvent("ender_bench_close");
    }

    public static void load() {
        EnderBench.LOGGER.log(Level.INFO, "Loading Sound Events");
    }
}
