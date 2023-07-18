package com.pie.loader;

import com.pie.common.loader.AlgorithmInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@ComponentScan("com.pie.loader")
@SpringBootTest(classes = PieDpcFactoryLoaderApplicationTests.class)
class PieDpcFactoryLoaderApplicationTests {


    @Autowired
    private ProgramLoader loader;
    @Test
    void contextLoads() {



    }

}
