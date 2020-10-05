package com.github.legoatoom.enderbench.block.entity;

import com.github.legoatoom.enderbench.EnderBench;
import com.github.legoatoom.enderbench.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

@SuppressWarnings("SameParameterValue")
public class ModBlockEntityType {
    public static final BlockEntityType<EnderBenchEntity> ENDER_BENCH_ENTITY;

    static{
        ENDER_BENCH_ENTITY = registerBlockEntity("ender_bench_entity");
    }

    private static BlockEntityType<EnderBenchEntity> registerBlockEntity(String name) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(EnderBench.MODID, name),
                BlockEntityType.Builder.create(EnderBenchEntity::new, ModBlocks.ENDER_BENCH).build(null));
    }

    public static void load() {
        EnderBench.LOGGER.log(Level.INFO, "Loading Block Entities");
    }
}
