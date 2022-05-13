package com.pie.dpc.notify.rabbitmq;

import com.pie.common.notify.MessageObj;
import com.pie.dpc.notify.SendNotifyFunction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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

    private String routeKey;
    private String exchange;

    public RabbitMQNotifySender(){
        this.routeKey = RabbitMQConfigure.DEFAULT_ROUTE_FILE_ARRIVAL;
        this.exchange = RabbitMQConfigure.DEFAULT_EXCHANGE_NAME;
    }

    public RabbitMQNotifySender(String routeKey, String directExchange) {

        if (StringUtils.isNotEmpty(routeKey)){
            this.routeKey = routeKey;
        }else {
            this.routeKey = RabbitMQConfigure.DEFAULT_ROUTE_FILE_ARRIVAL;
            log.debug("Empty ! use routeKey = {}",RabbitMQConfigure.DEFAULT_ROUTE_FILE_ARRIVAL);
        }
        if (StringUtils.isNotEmpty(directExchange)) {
            this.exchange = directExchange;
        }else {
            this.exchange = RabbitMQConfigure.DEFAULT_EXCHANGE_NAME;
            log.debug("Empty ! use directExchange = {}",RabbitMQConfigure.DEFAULT_EXCHANGE_NAME);
        }
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

//    public boolean sendNotifyMessage(String exchange,String routeKey,MessageObj notify) {
//        log.debug("Custom sender exchange = {} , routeKey = {}" ,exchange,routeKey);
//        try {
//            rabbitTemplate.convertAndSend(exchange, routeKey,notify.toString());
//        } catch (AmqpException e){
//            e.printStackTrace();
//            log.warn("RabbitMQ Sender Throws Exception ===> {} ",e.getMessage());
//            return false;
//        }
//        return true;
//    }



    @Override
    public boolean sendNotifyMessage(MessageObj notify) {
        try {
            log.debug("exchangeName = {} , routeKey = {}",this.exchange,this.routeKey);


            rabbitTemplate.convertAndSend(this.exchange,this.routeKey,new String(),msg->{
                notify.toMap().forEach((k,v) ->msg.getMessageProperties().setHeader(k,v));
                return msg;
            });
//            rabbitTemplate.convertAndSend(this.exchange, this.routeKey,notify.toString());
        } catch (AmqpException e){
            log.warn("RabbitMQ Sender Throws Exception ===> {} ",e.getMessage());
            return false;
        }
        return true;
    }
}
