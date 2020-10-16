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

package com.github.legoatoom.enderbench.client.network;

import com.github.legoatoom.enderbench.EnderBench;
import net.minecraft.util.Identifier;

public class PacketIDs {
    public static final Identifier OPEN_BENCH_PACKET_ID = new Identifier(EnderBench.MODID, "ender_bench_inventory_open");
    public static final Identifier CLOSE_BENCH_PACKET_ID = new Identifier(EnderBench.MODID, "ender_bench_inventory_close");

    public static final Identifier S2C_SETCONNECTION_PACKET_ID = new Identifier(EnderBench.MODID, "s2c_set_bench_connection_packet");
}
