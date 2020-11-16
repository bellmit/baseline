package org.benrush.baseline.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.common.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SourceCodeFileUtil {
    private static final String OUTPUT_LOCATION = ConfigUtil.getConfig(Constants.OUTPUT_LOCATION);

    private static final String SOURCE_CODE_LOCATION = FileUtil.getAbsolutePath(Constants.CODE_DIR);

    private static final String CHARSET = Constants.CODEC;

    private static final Integer SOURCE_PREFIX = SOURCE_CODE_LOCATION.length();

    private static final Integer OUTPUT_PREFIX = OUTPUT_LOCATION.length();

    private SourceCodeFileUtil(){

    }

    private static String transformLocation(String codeFileLocation){
        return OUTPUT_LOCATION + codeFileLocation.substring(SOURCE_PREFIX);
    }

    public static void test() throws IOException {
        log.info(OUTPUT_LOCATION);
        log.info(SOURCE_CODE_LOCATION);
        File codeDir = new File(SOURCE_CODE_LOCATION);
        writeFolder(codeDir);
    }

    /**
     * @Description: 将源代码文件夹中的文件解析并写入到目标文件夹
     * @author: 刘希晨
     * @date:  2020/11/16 11:26
     */
    public static void writeFile(File originalFile) throws IOException {
        if(FileUtil.isDirectory(originalFile)){
            throw new IOException("读取到了错误的文件夹而不是文件");
        }
        String originalPath = FileUtil.getAbsolutePath(originalFile);
        //通过更换前缀的方式转移文件的位置，得到新的路径
        String newPath = transformLocation(originalPath);
        //在新的路径下替换为新的文件名
        String newPathWithNewName = FormatUtil.process(newPath);
        List<String> originStrings = FileUtil.readLines(originalFile, "utf-8");
        File newFile = new File(newPathWithNewName);
        if(newFile.exists()){
            FileUtil.del(newFile);
        }
        newFile.createNewFile();
        List<String> formatStrings = new ArrayList<>();
        originStrings.forEach(s -> formatStrings.add(FormatUtil.process(s)));
        FileUtil.writeLines(formatStrings,newFile,CHARSET);
    }

    /**
     * @Description: 将目标文件夹中的所有文件都解析写入到目标文件夹，递归实现，深度优先
     * @author: 刘希晨
     * @date:  2020/11/16 11:27
     */
    public static void writeFolder(File originalFolder) throws IOException{
        if(!FileUtil.isDirectory(originalFolder)){
            throw new IOException("读取到了错误的文件而不是文件夹");
        }
        String originalPath = originalFolder.getAbsolutePath();
        //通过更换前缀的方式转移文件的位置，得到新的路径
        String newPath = transformLocation(originalPath);
        //在新的路径下替换为新的文件名
        String newPathWithNewName = FormatUtil.process(newPath);
        File newFolder = new File(newPathWithNewName);
        if(newFolder.exists()){
            FileUtil.del(newFolder);
        }
        newFolder.mkdir();
        File[] filesUnderFolder = FileUtil.ls(originalFolder.getPath());
        for(File file : filesUnderFolder){
            if(file.isDirectory()){
                writeFolder(file);
            }else{
                writeFile(file);
            }
        }
    }
}
