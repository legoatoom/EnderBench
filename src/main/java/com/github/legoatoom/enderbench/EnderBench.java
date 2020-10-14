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
