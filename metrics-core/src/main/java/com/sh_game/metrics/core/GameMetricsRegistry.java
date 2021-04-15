package com.sh_game.metrics.core;

import io.micrometer.core.instrument.MeterRegistry;

public interface GameMetricsRegistry {


    MeterRegistry registry();


    void init(ResourceConfig resourceConfig);



}
