package com.github.legoatoom.enderbench;

import com.github.legoatoom.enderbench.block.EnderBenchBlock;
import com.github.legoatoom.enderbench.block.ModBlocks;
import com.github.legoatoom.enderbench.block.entity.ModBlockEntityType;
import com.github.legoatoom.enderbench.item.ModItems;
import com.github.legoatoom.enderbench.screen.ModScreenHandlerType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
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


        ServerSidePacketRegistry.INSTANCE.register(ModConfigs.OPEN_BENCH_PACKET_ID, ((packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                World world = packetContext.getPlayer().world;
                BlockState state = world.getBlockState(pos);
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    packetContext.getPlayer().openHandledScreen(screenHandlerFactory);
                }
                EnderBenchBlock.playSound(world, ModConfigs.ENDER_BENCH_OPEN, packetContext.getPlayer().getBlockPos());
            });

        }));

        ServerSidePacketRegistry.INSTANCE.register(ModConfigs.CLOSE_BENCH_PACKET_ID, ((packetContext, packetByteBuf) -> {
            packetContext.getTaskQueue().execute(() -> {

                EnderBenchBlock.playSound(packetContext.getPlayer().world, ModConfigs.ENDER_BENCH_CLOSE, packetContext.getPlayer().getBlockPos());
            });

        }));

        Registry.register(Registry.SOUND_EVENT, ModConfigs.ENDER_BENCH_OPEN_SOUND_ID, ModConfigs.ENDER_BENCH_OPEN);
        Registry.register(Registry.SOUND_EVENT, ModConfigs.ENDER_BENCH_CLOSE_SOUND_ID, ModConfigs.ENDER_BENCH_CLOSE);

    }

}
