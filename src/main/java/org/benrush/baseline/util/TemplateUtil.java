package org.benrush.baseline.util;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.common.Constants;
import org.benrush.baseline.database.DatabaseConnector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用于freemarker的模板代码加载与编译
 * @author: 刘希晨
 * @date:  2020/11/16 14:22
 */
@Slf4j
public class TemplateUtil {
    private TemplateUtil(){

    }

    private static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);

    static {
        try{
            cfg.setDirectoryForTemplateLoading(FileUtil.file("/templates"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }catch (Exception e){
            log.error("加载freemarker配置出错");
            e.printStackTrace();
        }
    }

    public static Map<String,Object> getBaseMap(){
        Map<String,Object> baseMap = new HashMap<>();
        //项目配置
        Map<String,Object> projectMap = new HashMap<>();
        projectMap.put("name",ConfigUtil.getConfig(Constants.NAME));
        projectMap.put("groupId",ConfigUtil.getConfig(Constants.GROUPID));

        baseMap.put("project",projectMap);
        return baseMap;
    }

    public static void render(String templateFile, File out,Map<String, Object> params) throws IOException, TemplateException {
        Template template = cfg.getTemplate(templateFile);
        Writer writer = new FileWriter(out);
        template.process(params,writer);
    }

    public static void renderPoClass(String tableName){
        Map<String, Object> map = getBaseMap();
        map.put("tableName",tableName);
        map.put("tableComment",DatabaseUtil.getPoComment(tableName));
        map.put("tableClass",DatabaseUtil.UpperTheFirstLetter(DatabaseUtil.removePrefix(tableName)));
        map.put("poItemList",DatabaseUtil.getPoColumnInfo(tableName));
    }
}
