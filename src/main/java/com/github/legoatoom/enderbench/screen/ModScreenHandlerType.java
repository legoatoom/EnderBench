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
