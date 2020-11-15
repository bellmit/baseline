package org.benrush;

import lombok.extern.slf4j.Slf4j;
import org.benrush.util.SouceCodeFileUtil;

import java.io.File;
import java.io.IOException;

@Slf4j
public class MainApplication {
    public static void main(String[] args) throws IOException {
        String s = MainApplication.class.getClassLoader().getResource("source-code").getPath();
        File file = new File(s + File.separator + "${test}.java");
        SouceCodeFileUtil.writeFile(file);
    }
}
