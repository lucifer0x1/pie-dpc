package com.pie.dpc.notify;

import com.pie.common.notify.MessageObj;
import com.pie.dpc.filelistener.AfterFileNotify;
import com.pie.dpc.filelistener.FileDirectoryMonitorService;
import com.pie.dpc.notify.rabbitmq.RabbitMQNotifySender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
@SpringBootApplication
class RabbitMQNotifySenderTest {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQNotifySenderTest.class,args);
    }

    @Autowired
    RabbitMQNotifySender sender;

    @Test
    public void sendMessage(){
        for (int i = 0; i < 10; i++) {
            System.out.println(sender.sendNotifyMessage(new MessageObj()));
        }

    }

    @Test
    public void starterTest(){
        FileDirectoryMonitorService listener = new FileDirectoryMonitorService(new AfterFileNotify() {
            @Override
            public void afterFileNotifyFunction(File file) {
                System.out.println(file.getAbsolutePath());
            }
        });
        listener.monitor("c:\\test\\a","c:\\test\\b");
    }
}