package com.pie.dpc.notify;

import com.pie.common.notify.MessageObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/6 23:52
 * @Description TODO :
 *
 * MQ消息发送
 **/
@Component
public class RabbitMQNotifySender implements SendNotifyFunction {
    Logger log = LoggerFactory.getLogger(RabbitMQNotifySender.class);

    private String routeKey = null;



    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    public boolean sendNotifyMessage(MessageObj notify) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfigure.DEFAULT_EXCHANGE_NAME,
                    RabbitMQConfigure.DEFAULT_ROUTE_FILE_ARRIVAL,notify.toString());
        } catch (AmqpException e){
            e.printStackTrace();
            log.warn("RabbitMQ Sender Throws Exception ===> {} ",e.getMessage());
            return false;
        }
        return true;
    }
}
