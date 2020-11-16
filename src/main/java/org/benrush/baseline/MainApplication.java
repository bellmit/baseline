package org.benrush.baseline;

import lombok.extern.slf4j.Slf4j;
import org.benrush.baseline.util.SourceCodeFileUtil;

import java.io.IOException;

@Slf4j
public class MainApplication {
    public static void main(String[] args) throws IOException {
        SourceCodeFileUtil.test();
    }
}
