package com.github.legoatoom.enderbench.mixin.client.network;

import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;

public interface IClientPlayerEntity {


    EnderBenchEntity getConnectedBench();

    void setConnectedBench();
}
