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

import static com.sh_game.metrics.core.registry.Configs.PrometheusPort;

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

        //默认断开
        Integer port = null;

        Map<String, String> configDatas = resourceConfig.getAll();


        String configPort = configDatas.get(PrometheusPort);
        if (Strings.isNullOrEmpty(configPort)) {
            port = DefaultPrometheus.metrics_prot;
            resourceConfig.resetConfig(PrometheusPort, String.valueOf(port));//重新回写到配置
        }else{
            port = Integer.parseInt(configPort);
        }


        String url = configDatas.get(Configs.PrometheusUrl);
        if (Strings.isNullOrEmpty(url)) {
            url = DefaultPrometheus.metrics_Url;
        }

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


}
