package com.sh_game.metrics.core;

import com.sh_game.metrics.core.registry.SimpleGameRegistry;
import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.*;
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
         * 查找实现类
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


        /**
         * 读取配置
         * 1、先从文件获取，
         * 2、文件没有的话，传入空配置，
         * 空配置：实现类对于空配置一般建议先-D参数获取，然后在使用默认参数,
         * 使用之后，建议赋值，用于其他组件获取当前使用配置
         */
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


    /***
     * 剂量计包装类
     */

    public Counter counter(String name, Iterable<Tag> tags) {
        return INSTANCE.counter(name, tags);
    }

    public Counter counter(String name, String... tags) {
        return INSTANCE.counter(name, tags);
    }

    public DistributionSummary summary(String name, Iterable<Tag> tags) {
        return INSTANCE.summary(name, tags);
    }

    public DistributionSummary summary(String name, String... tags) {
        return INSTANCE.summary(name, tags);
    }

    public Timer timer(String name, Iterable<Tag> tags) {
        return INSTANCE.timer(name, tags);
    }

    public Timer timer(String name, String... tags) {
        return INSTANCE.timer(name, tags);
    }

    public <T extends Number> T gauge(String name, Iterable<Tag> tags, T number) {
        return INSTANCE.gauge(name, tags, number);
    }

    public <T extends Number> T gauge(String name, T number) {
        return INSTANCE.gauge(name, number);
    }

}
