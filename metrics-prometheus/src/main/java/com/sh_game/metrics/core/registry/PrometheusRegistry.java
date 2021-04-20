package com.sh_game.metrics.core.registry;

import com.google.common.base.Strings;
import com.sh_game.metrics.core.GameMetricsRegistry;
import com.sh_game.metrics.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;


/**
 * 基于普罗米修斯的实现
 *
 * @author SilenceSu
 * @Email Silence.Sx@Gmail.com
 * Created by Silence on 2021/4/14.
 */
public class PrometheusRegistry implements GameMetricsRegistry {

    private final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @Override
    public MeterRegistry registry() {
        return registry;
    }

    @Override
    public void init(ResourceConfig resourceConfig) {

        int port = getPort(resourceConfig);
        String url = getUrl(resourceConfig);


        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext(url, httpExchange -> {
                String response = registry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });
            new Thread(server::start).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private int getPort(ResourceConfig resourceConfig) {
        Map<String, String> configDatas = resourceConfig.getAll();
        String configPort = configDatas.get(Configs.PrometheusPort);

        if (Strings.isNullOrEmpty(configPort)) {
            configPort = (System.getProperty(Configs.PrometheusPort, String.valueOf(DefaultPrometheus.metrics_prot)));
            resourceConfig.resetConfig(Configs.PrometheusPort, configPort);//重新回写到配置
        }
        return Integer.parseInt(configPort);
    }

    private String getUrl(ResourceConfig resourceConfig) {
        Map<String, String> configDatas = resourceConfig.getAll();

        //读取url配置
        String url = configDatas.get(Configs.PrometheusUrl);
        if (Strings.isNullOrEmpty(url)) {
            url = DefaultPrometheus.metrics_Url;
            resourceConfig.resetConfig(Configs.PrometheusUrl, url);//重新回写到配置
        }

        return url;
    }


}
