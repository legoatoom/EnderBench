package com.github.legoatoom.enderbench.block;


import com.github.legoatoom.enderbench.EnderBench;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

@SuppressWarnings("SameParameterValue")
public class ModBlocks {
    public static final Block ENDER_BENCH;

    private static Block registerBlock(String name, Block block){
        return Registry.register(Registry.BLOCK, new Identifier(EnderBench.MODID, name), block);
    }

    static{
        ENDER_BENCH = registerBlock("ender_bench", new EnderBenchBlock(FabricBlockSettings.of(Material.STONE).strength(2.5f)));

    }

    public static void load(){
        EnderBench.LOGGER.log(Level.INFO, "Loading Blocks");
    }
}
