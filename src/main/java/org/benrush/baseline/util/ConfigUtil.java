package org.benrush.baseline.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigUtil {
    private ConfigUtil() {

    }

    private static final Properties properties = new Properties();

    static {
        InputStream in = FileUtil.getInputStream("application.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("未找到配置文件");
        }
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }


}
