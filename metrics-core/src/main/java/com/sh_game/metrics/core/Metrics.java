package com.sh_game.metrics.core;

import com.sh_game.metrics.core.registry.SimpleGameRegistry;
import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;

import java.util.ServiceLoader;

public class Metrics {

    /**
     * registry 实例
     */
    private static final MeterRegistry INSTANCE;


    private final static ResourceConfig resourceConfig;

    static {

        /**
         * 查找
         */
        ServiceLoader<GameMetricsRegistry> registries = ServiceLoader.load(GameMetricsRegistry.class);

        GameMetricsRegistry registry = null;
        for (GameMetricsRegistry metricsRegistry : registries) {
            registry = metricsRegistry;
            break;
        }

        if (registry == null) {
            registry = new SimpleGameRegistry();
        }


        ResourceConfig config = null;
        try {
            config = ResourceConfig.getOrNull(MetricsConstants.CONFIG_FILE);
        } catch (Exception e) {
            config = ResourceConfig.getEmpty();
        }

        resourceConfig = config;

        //初始化
        registry.init(resourceConfig);

        INSTANCE = registry.registry();


        bindJvm(INSTANCE);
    }


    private static void bindJvm(MeterRegistry instance) {
        new JvmMemoryMetrics().bindTo(instance);
        new JvmGcMetrics().bindTo(instance);
        new ProcessMemoryMetrics().bindTo(instance);
        new ProcessThreadMetrics().bindTo(instance);
        new ProcessorMetrics().bindTo(instance);
        new JvmThreadMetrics().bindTo(instance);
    }

    public static MeterRegistry meter() {
        return INSTANCE;
    }

    public static ResourceConfig getResourceConfig() {
        return resourceConfig;
    }
}
