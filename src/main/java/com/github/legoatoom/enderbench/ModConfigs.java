package com.github.legoatoom.enderbench;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModConfigs {
    public static final double EnderBenchRange = 32D;
    public static final int EnderBenchSize = 15;
    public static final Identifier OPEN_BENCH_PACKET_ID = new Identifier(EnderBench.MODID, "ender_bench_inventory_open");
    public static final Identifier CLOSE_BENCH_PACKET_ID = new Identifier(EnderBench.MODID, "ender_bench_inventory_close");
    public static final Identifier ENDER_BENCH_OPEN_SOUND_ID = new Identifier(EnderBench.MODID, "ender_bench_open");
    public static SoundEvent ENDER_BENCH_OPEN = new SoundEvent(ENDER_BENCH_OPEN_SOUND_ID);
    public static final Identifier ENDER_BENCH_CLOSE_SOUND_ID = new Identifier(EnderBench.MODID, "ender_bench_close");
    public static SoundEvent ENDER_BENCH_CLOSE = new SoundEvent(ENDER_BENCH_CLOSE_SOUND_ID);
}
