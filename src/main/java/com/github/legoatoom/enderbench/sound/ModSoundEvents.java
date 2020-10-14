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
