package com.pie.common.heartbeat;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan("com.pie.common")

class RedisHeartBeatMonitorTest {


    @Autowired
    RedisHeartBeatMonitor monitor;

    @Test
    public void testHearBeat(){
        monitor.autoStart();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}