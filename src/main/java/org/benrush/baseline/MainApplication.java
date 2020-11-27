package org.benrush.baseline;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.util.SourceCodeFileUtil;
import org.benrush.baseline.util.TemplateUtil;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class MainApplication {
    public static void main(String[] args) throws IOException, TemplateException, SQLException {
        long start = System.currentTimeMillis();
        SourceCodeFileUtil.constructProgramSkeleton();
        TemplateUtil.renderAllTemplates();
        long end = System.currentTimeMillis();
        log.info("完成项目生成，用时：" + (end-start)/1000 + " second");
    }
}
