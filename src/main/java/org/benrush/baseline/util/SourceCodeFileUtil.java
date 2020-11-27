package org.benrush.baseline.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.common.BaseLineException;
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

    private static final String SEP = System.getProperty("os.name").contains("Windows") ? "\\" : "/";

    private static final String PROJECT_NAME = "${project.name}";



    //true表示覆盖，false表示追加
    private static final Boolean OVERWIRTE = Integer.valueOf(ConfigUtil.getConfig(Constants.MODE))==0;

    private SourceCodeFileUtil(){

    }

    /**
     * @Description: 构建代码项目
     * @author: 刘希晨
     * @date:  2020/11/17 17:38
     */
    public static void constructProgramSkeleton() throws IOException {
        File root = new File(OUTPUT_LOCATION);
        if(!FileUtil.isDirectory(root)){
            throw new BaseLineException("输出目的地并不是文件夹");
        }
        writeFolder(new File(SOURCE_CODE_LOCATION.concat(SEP).concat(PROJECT_NAME)),OUTPUT_LOCATION);
    }

    public static String createFolder(File root,String folderName){
        File newFolder = new File(root.getAbsolutePath() + SEP + folderName);
        log.info(newFolder.getAbsolutePath());
        if(newFolder.exists()){
            if(OVERWIRTE){
                FileUtil.del(newFolder);
                FileUtil.mkdir(newFolder);
            }
        }else{
            FileUtil.mkdir(newFolder);
        }
        return newFolder.getAbsolutePath();
    }

    /**
     * @Description: 将文件解析并写入到指定的文件夹路径下,可手动指定新文件的名字
     * @author: 刘希晨
     * @date:  2020/11/16 11:26
     */
    public static void writeFile(File originalFile, String folderPath, String newName) throws IOException {
        if(FileUtil.isDirectory(originalFile)){
            throw new IOException("读取到了错误的文件夹而不是文件");
        }
        String originalFileName = FileUtil.getName(originalFile);
        String newFileName;
        if(newName != null){
            newFileName = newName;
        }else{
            newFileName = FormatUtil.process(originalFileName);
        }
        String newPath = folderPath + SEP + newFileName;
        log.info("文件写入 " + newPath);
        File newFile = FileUtil.newFile(newPath);
        if(newFile.exists()){
            if(OVERWIRTE){
                FileUtil.del(newFile);
                newFile.createNewFile();
            }
        }
        else{
            newFile.createNewFile();
        }
        List<String> originalStrings = FileUtil.readLines(originalFile,CHARSET);
        List<String> formatStrings = new ArrayList<>();
        originalStrings.forEach(s -> formatStrings.add(FormatUtil.process(s)));
        FileUtil.writeLines(formatStrings,newFile,CHARSET);
    }

    public static void writeFile(File originalFile, String folderPath) throws IOException {
        writeFile(originalFile,folderPath,null);
    }



    /**
     * @Description: 将目标文件夹中的所有文件都解析写入到目标文件夹路径下，递归实现，深度优先
     * @author: 刘希晨
     * @date:  2020/11/16 11:27
     */
    public static void writeFolder(File originalFolder, String folderPath) throws IOException{
        if(!FileUtil.isDirectory(originalFolder)){
            throw new IOException("读取到了错误的文件而不是文件夹");
        }
        String originalName = FileUtil.getName(originalFolder);
        String newFolderName = FormatUtil.process(originalName);
        String newPath = folderPath.concat(SEP).concat(newFolderName);
        log.info("文件夹写入 " + newPath);
        File newFolder = new File(newPath);
        if(FileUtil.exist(newFolder)){
            if(OVERWIRTE){
                FileUtil.del(newFolder);
                FileUtil.mkdir(newFolder);
            }
        }else{
            FileUtil.mkdir(newFolder);
        }
        File[] filesUnderFolder = FileUtil.ls(FileUtil.getAbsolutePath(originalFolder));
        for(File file : filesUnderFolder){
            if(file.isDirectory()){
                writeFolder(file,newPath);
            }else{
                writeFile(file,newPath);
            }
        }
    }
}
