package org.benrush.baseline.util;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.common.Constants;
import org.benrush.baseline.pojo.PoItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @Description: 用于freemarker的模板代码加载与编译
 * @author: 刘希晨
 * @date:  2020/11/16 14:22
 */
@Slf4j
public class TemplateUtil {
    private TemplateUtil(){

    }

    private static final Integer MODE = Integer.valueOf(ConfigUtil.getConfig(Constants.FREEMARKER_MODE));

    private static final Configuration CFG = new Configuration(Configuration.VERSION_2_3_30);

    private static final List<String> BASE_PO_COLUMN = Arrays.asList("id","createTime","modifyTime","deleted");

    private static final String API_LOCATION = "${output.location}\\${project.name}\\${project.name}-api\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}\\facade\\api\\";
    private static final String CONTROLLER_LOCATION = "${output.location}\\${project.name}\\${project.name}-server\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}}\\server\\controller\\";
    private static final String MAPPER_LOCATION = "${output.location}\\${project.name}\\${project.name}-server\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}}\\server\\persistence\\mapper\\";
    private static final String PO_LOCATION = "${output.location}\\${project.name}\\${project.name}-server\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}}\\server\\persistence\\po\\";
    private static final String REQDTO_LOCATION = "${output.location}\\${project.name}\\${project.name}-api\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}}\\facade\\dto\\req\\";
    private static final String RESDTO_LOCATION = "${output.location}\\${project.name}\\${project.name}-api\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}}\\facade\\dto\\res\\";
    private static final String SERVICE_LOCATION = "${output.location}\\${project.name}\\${project.name}-server\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}}\\server\\service\\defination\\";
    private static final String SERVICEIMPL_LOCATION = "${output.location}\\${project.name}\\${project.name}-server\\src\\main\\java\\${groupId.first}\\${groupId.second}\\${project.name}}\\server\\service\\impl\\";


    static {
        try{
            CFG.setDirectoryForTemplateLoading(FileUtil.file("templates"));
            CFG.setDefaultEncoding("UTF-8");
            CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }catch (Exception e){
            log.error("加载freemarker配置出错");
            e.printStackTrace();
        }
    }
    
    public static void renderAllTemplates() throws IOException, TemplateException {
        final String[] tables = DatabaseUtil.getTables();
        for(String tableName : tables){
            renderApi(tableName);
            renderController(tableName);
            renderReqDto(tableName);
            renderResDto(tableName);
            renderPoClass(tableName);
            renderMapper(tableName);
            renderService(tableName);
            renderServiceImpl(tableName);
        }
    }

    /**
     * @Description: 根据表名获取一些基础的配置信息，可供生成通用
     * @author: 刘希晨
     * @date:  2020/11/27 11:56
     */
    public static Map<String,Object> getBaseMap(String tableName){
        Map<String,Object> baseMap = new HashMap<>();
        //项目配置
        Map<String,Object> projectMap = new HashMap<>();
        projectMap.put("name",ConfigUtil.getConfig(Constants.NAME));
        projectMap.put("groupId",ConfigUtil.getConfig(Constants.GROUP_ID));
        baseMap.put("project",projectMap);
        //prefix_some_table => someTable
        String baseName = FormatUtil.underline2Camel(DatabaseUtil.removePrefix(tableName));
        //类名的首字母大写
        String baseClass = DatabaseUtil.UpperTheFirstLetter(baseName);

        baseMap.put("baseName",baseName);
        baseMap.put("reqName",baseClass.concat("ReqDto"));
        baseMap.put("resName",baseClass.concat("ResDto"));
        baseMap.put("serviceName",baseClass.concat("Service"));
        baseMap.put("apiName",baseClass.concat("Api"));
        baseMap.put("poName",baseClass.concat("Po"));
        baseMap.put("serviceImplName",baseClass.concat("ServiceImpl"));
        baseMap.put("controllerName",baseClass.concat("Controller"));
        baseMap.put("mapperName",baseClass.concat("Mapper"));
        return baseMap;
    }

    public static void render(String templateFile, File outputFile,Map<String, Object> params) throws IOException, TemplateException {
        Template template = CFG.getTemplate(templateFile);
        Writer writer = new FileWriter(outputFile);
        template.process(params,writer);
    }

    /**
     * @Description: 渲染po类
     * @author: 刘希晨
     * @date:  2020/11/27 10:14
     */
    public static void renderPoClass(String tableName) throws IOException, TemplateException {
        Map<String, Object> map = getBaseMap(tableName);
        map.put("tableComment",DatabaseUtil.getPoComment(tableName));
        map.put("poItemList",DatabaseUtil.getPoColumnInfo(tableName));
        File outputFile = FileUtil.file(FormatUtil.process(PO_LOCATION) + map.get("poName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建po成功");
        } else {
            log.error("创建po失败");
        }
        render("po.ftl",outputFile,map);
    }

    /**
     * @Description: 渲染reqDto类
     * @author: 刘希晨
     * @date:  2020/11/27 10:35
     */
    public static void renderReqDto(String tableName) throws IOException, TemplateException{
        Map<String, Object> map = getBaseMap(tableName);
        map.put("tableComment",DatabaseUtil.getPoComment(tableName));
        map.put("reqItemList",filterBasePoColumn(DatabaseUtil.getPoColumnInfo(tableName)));
        File outputFile = FileUtil.file(FormatUtil.process(REQDTO_LOCATION) + map.get("reqName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建reqDto成功");
        } else {
            log.error("创建reqDto失败");
        }
        render("reqdto.ftl",outputFile,map);
    }

    /**
     * @Description: 渲染resDto类
     * @author: 刘希晨
     * @date:  2020/11/27 10:35
     */
    public static void renderResDto(String tableName) throws IOException, TemplateException{
        Map<String, Object> map = getBaseMap(tableName);
        map.put("tableComment",DatabaseUtil.getPoComment(tableName));
        List<PoItem> resDtoItems = DatabaseUtil.getPoColumnInfo(tableName);
        //resDto除了假删除字段没有以外，其他与po一致
        resDtoItems.removeIf(item -> item.getPropertyName().equals("deleted"));
        map.put("resItemList",resDtoItems);
        File outputFile = FileUtil.file(FormatUtil.process(RESDTO_LOCATION) + map.get("resName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建resDto成功");
        } else {
            log.error("创建resDto失败");
        }
        render("resdto.ftl",outputFile,map);
    }

    /**
     * @Description: 渲染api类
     * @author: 刘希晨
     * @date:  2020/11/27 10:35
     */
    public static void renderApi(String tableName) throws IOException, TemplateException{
        Map<String, Object> map = getBaseMap(tableName);
        File outputFile = FileUtil.file(FormatUtil.process(API_LOCATION) + map.get("apiName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建api成功");
        } else {
            log.error("创建api失败");
        }
        render("api.ftl",outputFile,map);
    }

    /**
     * @Description: 渲染api类
     * @author: 刘希晨
     * @date:  2020/11/27 10:35
     */
    public static void renderController(String tableName) throws IOException, TemplateException{
        Map<String, Object> map = getBaseMap(tableName);
        File outputFile = FileUtil.file(FormatUtil.process(CONTROLLER_LOCATION) + map.get("controllerName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建controller成功");
        } else {
            log.error("创建controller失败");
        }
        render("controller.ftl",outputFile,map);
    }

    /**
     * @Description: 渲染service类
     * @author: 刘希晨
     * @date:  2020/11/27 10:35
     */
    public static void renderService(String tableName) throws IOException, TemplateException{
        Map<String, Object> map = getBaseMap(tableName);
        File outputFile = FileUtil.file(FormatUtil.process(SERVICE_LOCATION) + map.get("serviceName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建service成功");
        } else {
            log.error("创建service失败");
        }
        render("service.ftl",outputFile,map);
    }

    /**
     * @Description: 渲染serviceimpl类
     * @author: 刘希晨
     * @date:  2020/11/27 10:35
     */
    public static void renderServiceImpl(String tableName) throws IOException, TemplateException{
        Map<String, Object> map = getBaseMap(tableName);
        File outputFile = FileUtil.file(FormatUtil.process(SERVICEIMPL_LOCATION) + map.get("serviceImplName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建serviceimpl成功");
        } else {
            log.error("创建serviceimpl失败");
        }
        render("serviceimpl.ftl",outputFile,map);
    }

    /**
     * @Description: 渲染serviceimpl类
     * @author: 刘希晨
     * @date:  2020/11/27 10:35
     */
    public static void renderMapper(String tableName) throws IOException, TemplateException{
        Map<String, Object> map = getBaseMap(tableName);
        File outputFile = FileUtil.file(FormatUtil.process(MAPPER_LOCATION) + map.get("serviceImplName") + ".java");
        if(FileUtil.exist(outputFile)){
            if(MODE == 1) return;
        }
        if (outputFile.createNewFile()) {
            log.info("创建mapper成功");
        } else {
            log.error("创建mapper失败");
        }
        render("mapper.ftl",outputFile,map);
    }


    /**
     * @Description: 过滤basepo中的字段，reqDto不需要这些字段，这些采用自动生成的方式
     * @author: 刘希晨
     * @date:  2020/11/27 10:31
     */
    public static List<PoItem> filterBasePoColumn(List<PoItem> list){
        list.removeIf(poItem -> BASE_PO_COLUMN.contains(poItem.getPropertyName()));
        return list;
    }
}
