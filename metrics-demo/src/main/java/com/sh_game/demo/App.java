package com.sh_game.demo;

import com.sh_game.metrics.core.Metrics;
import io.micrometer.core.instrument.Counter;

import java.util.Map;

public class App {
    public static void main(String[] args) {

        System.out.println("demo");
        Counter counted = Metrics.counter("name.dd.ad");
        counted.increment();


        Map<String, String> configs = Metrics.getResourceConfig().getAll();
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            System.out.println("当前使用配置 key:" + entry.getKey() + ",value={" + entry.getValue() + "}");
        }


    }
}
