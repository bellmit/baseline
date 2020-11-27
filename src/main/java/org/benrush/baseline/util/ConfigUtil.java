package org.benrush.baseline.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.common.Constants;

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
        //处理自定义的properties项
        String groupId = properties.getProperty(Constants.GROUP_ID);
        String[] s = groupId.split("\\.");
        properties.put(Constants.GROUP_FIRST,s[0]);
        properties.put(Constants.GROUP_SECOND,s[1]);
        String name = properties.getProperty(Constants.NAME);
        properties.put(Constants.CLASS,DatabaseUtil.UpperTheFirstLetter(name));
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }


}
