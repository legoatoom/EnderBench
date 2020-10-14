package com.github.legoatoom.enderbench.client;

import com.github.legoatoom.enderbench.screen.ModScreenHandlerType;
import com.github.legoatoom.enderbench.screen.ingame.EnderBenchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.options.KeyBinding;

@Environment(EnvType.CLIENT)
public class EnderBenchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ModScreenHandlerType.ENDER_BENCH_SCREEN_HANDLER_SCREEN_HANDLER, EnderBenchScreen::new);
    }
}
