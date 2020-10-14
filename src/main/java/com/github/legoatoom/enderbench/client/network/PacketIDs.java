package com.github.legoatoom.enderbench.client.network;

import com.github.legoatoom.enderbench.EnderBench;
import net.minecraft.util.Identifier;

public class PacketIDs {
    public static final Identifier OPEN_BENCH_PACKET_ID = new Identifier(EnderBench.MODID, "ender_bench_inventory_open");
    public static final Identifier CLOSE_BENCH_PACKET_ID = new Identifier(EnderBench.MODID, "ender_bench_inventory_close");
}
