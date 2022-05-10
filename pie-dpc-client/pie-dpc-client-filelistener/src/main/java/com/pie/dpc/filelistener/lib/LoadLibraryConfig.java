package com.pie.dpc.filelistener.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * ClassName LoadLibraryConfig
 * Description
 * Date 2022/5/9
 * Author wangxiyue.xy@163.com
 */
@Configuration
@Component
public class LoadLibraryConfig {

    static Logger logger = LoggerFactory.getLogger(LoadLibraryConfig.class);

    private static final String LIB_JNOTIFY_TARGET_PATH = "lib/libjnotify.so";




    private static String extractLibrary() {
        try {
            // Create temporary file
            File file = File.createTempFile("libnotify", ".lib");
            // In case it worked, we can extract lib
            if (file.exists()) {
                // First of all, let's show where do we plan to extract it
                logger.debug("Temporary file: {}" , file.getAbsoluteFile().toPath());
                // Now, we can get the lib file from JAR and put it inside temporary location
                InputStream link = (LoadLibraryConfig.class.getClassLoader().getResourceAsStream(LIB_JNOTIFY_TARGET_PATH));
                // We want to overwrite existing file. This is why we are using
                // java.nio.file.StandardCopyOption.REPLACE_EXISTING
                Files.copy(
                        link,
                        file.getAbsoluteFile().toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                return file.getAbsoluteFile().toPath().toString();
            }
            // In case something goes wrong, we will simply return null
            return null;
        } catch (IOException e) {
            // The same goes for exception - we are passing null back
            logger.error("ext library error : {} ",e.getMessage());
            return null;
        }
    }

    @PostConstruct
    public static void initLoad(){

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            logger.debug("Not load Library Running on win32 => {} ",osName);
        }else if (osName.startsWith("linux")) {
            logger.debug("Loading.... Running on Linux => {} ",osName);
            String filePath = extractLibrary();
            if(filePath!=null){
                System.load(filePath);
            }else{
                logger.error("extractLibrary un success");
            }
        }


    }

}
