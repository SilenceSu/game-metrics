package com.sh_game.demo;

import com.sh_game.metrics.core.Metrics;
import com.sh_game.metrics.core.registry.DefaultPrometheus;
import io.micrometer.core.instrument.Counter;

public class App {
    public static void main(String[] args) {

        System.out.println("demo");
        DefaultPrometheus.metrics_prot = 3334;
        Counter counted = Metrics.meter().counter("name.dd.ad");
        counted.increment();


    }
}
