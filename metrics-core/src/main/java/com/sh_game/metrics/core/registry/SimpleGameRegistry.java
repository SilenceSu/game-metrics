package com.sh_game.metrics.core.registry;

import com.sh_game.metrics.core.GameMetricsRegistry;
import com.sh_game.metrics.core.ResourceConfig;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public class SimpleGameRegistry implements GameMetricsRegistry {



    @Override
    public MeterRegistry registry() {
        return new SimpleMeterRegistry();
    }

    @Override
    public void init(ResourceConfig resourceConfig) {

    }

}
