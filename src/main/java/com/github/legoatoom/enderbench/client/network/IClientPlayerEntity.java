package com.github.legoatoom.enderbench.client.network;

import com.github.legoatoom.enderbench.block.EnderBenchBlock;
import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;
import net.minecraft.client.render.SkyProperties;


public interface IClientPlayerEntity {


    public boolean hasConnection();

    public EnderBenchEntity getConnectedBench();

    public void setConnectedBench(EnderBenchEntity enderBenchEntity);

}
