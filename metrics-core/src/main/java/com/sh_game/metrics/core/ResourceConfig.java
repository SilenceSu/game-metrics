package com.sh_game.metrics.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 读取文件
 */
public class ResourceConfig {

    private static Logger logger = LoggerFactory.getLogger(ResourceConfig.class);

    private final Map<String, String> data;

    public static ResourceConfig getOrNull(final String name) throws Exception {
        try {
            return new ResourceConfig(name);
        } catch (ResourceException e) {
            throw new RuntimeException("检测到重复配置文件", e);
        } catch (Exception e) {
            throw new Exception("读取文件失败");
         }
    }

    public static ResourceConfig getEmpty(){
        return new ResourceConfig();
    }


    private ResourceConfig() {
        data = Maps.newHashMap();
    }

    private ResourceConfig(final String name) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "配置文件名不能为空");
        forbidDuplicateConfig(name);

        try (InputStream res = Thread.currentThread().getContextClassLoader().getResourceAsStream(name)) {
            if (res == null) {
                throw new RuntimeException("无法找到配置文件: " + name);
            }
            final Properties prop = new Properties();
            prop.load(res);
            data = fromProperties(prop);
        } catch (Exception e) {
            throw new RuntimeException("无法读取配置文件：" + name, e);
        }
    }

    private static void forbidDuplicateConfig(final String name) {
        try {
            final List<URL> resources = Collections
                    .list(Thread.currentThread().getContextClassLoader().getResources(name));
            if (resources.size() > 1) {
                logger.error("文件{}只允许有一个，但是发现多个，位置分别为: {}", name, resources);
                throw new ResourceException("配置文件" + name + "不能存在多个，地址分别为：" + resources);
            }
        } catch (IOException e) {
            // do nothing here
        }
    }

    private Map<String, String> fromProperties(final Properties prop) {
        final Map<String, String> map = Maps.newHashMap();
        for (final String key : prop.stringPropertyNames()) {
            map.put(key, prop.getProperty(key));
        }
        return map;
    }

    public Map<String, String> getAll() {
        return Collections.unmodifiableMap(data);
    }


    public void resetConfig(String key,String value){
        if (data != null) {
            data.put(key, value);
        }
    }


}
