package com.pie.dpc.notify;

import com.pie.common.notify.MessageObj;
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
}