package com.pie.dpc.notify;

import com.pie.common.notify.MessageObj;
import com.pie.dpc.filelistener.FileDirectoryMonitorService;
import com.pie.dpc.notify.rabbitmq.RabbitMQNotifySender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitMQNotifySenderTest {

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
        FileDirectoryMonitorService listener = new FileDirectoryMonitorService();
        listener.monitor("c:\\test\\a","c:\\test\\b");
    }
}