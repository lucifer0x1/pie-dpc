package com.pie.loader;

import com.pie.common.loader.AlgorithmInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MonitorLoader {

    @Autowired
    ProgramLoader loader;

//    @PostConstruct
    public void monitor(){
        /**
         * String absolutePath,
         * String fileName,
         * String jarClassName,
         */
        AlgorithmInterface impl = loader.loadByJar("D:\\IdeaProjects\\pie-dpc\\pie-dpc\\pie-algorithm\\simple-algorithm\\out\\",
                "simple-algorithm-1.0-dev.jar",
                "com.pie.common.loader.AlgorithmInterface");
        impl.exe("123");

    }
}
