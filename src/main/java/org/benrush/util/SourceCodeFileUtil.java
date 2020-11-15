package org.benrush.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.benrush.MainApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SourceCodeFileUtil {
    private static final String LOCATION = ConfigUtil.getConfig("output.location");

    private static final String SOURCE_CODE_LOCATION = FileUtil.getAbsolutePath("code");


    private static final String CHARSET = "utf-8";

    private static final Integer PREFIX_LENGTH = SOURCE_CODE_LOCATION.length();

    private SourceCodeFileUtil(){

    }

    private static String transformLocation(String codeFileLocation){
        return LOCATION + codeFileLocation.substring(PREFIX_LENGTH);
    }

    public static void test(){
        File[] ls = FileUtil.ls(SOURCE_CODE_LOCATION);
        for(File file : ls){
            log.info(file.getAbsolutePath());
            log.info(transformLocation(file.getAbsolutePath()));
        }
    }

    public static void writeFile(File originFile) throws IOException {
        writeFile(originFile,transformLocation(originFile.getPath()));
    }

    public static void writeFile(File originalFile, String path) throws IOException {
        if(FileUtil.isDirectory(originalFile)){
            throw new IOException("读取到了错误的文件夹而不是文件");
        }
        String newFileName = FormatUtil.process(FileUtil.getName(originalFile));
        List<String> originStrings = FileUtil.readLines(originalFile, "utf-8");
        File newFile = new File(path + File.separator + newFileName);
        if(newFile.exists()){
            newFile.delete();
        }
        newFile.createNewFile();
        List<String> formatStrings = new ArrayList<>();
        originStrings.forEach(s -> formatStrings.add(FormatUtil.process(s)));
        FileUtil.writeLines(formatStrings,newFile,CHARSET);
    }

    public static void writeFolder(File originalFolder, String path) throws IOException{
        if(!FileUtil.isDirectory(originalFolder)){
            throw new IOException("读取到了错误的文件而不是文件夹");
        }
        File[] filesUnderFolder = FileUtil.ls(originalFolder.getPath());
        for(File file : filesUnderFolder){
            if(file.isDirectory()){
                writeFolder(file,transformLocation(file.getPath()));
            }else{
                writeFile(file);
            }
        }
    }
}
