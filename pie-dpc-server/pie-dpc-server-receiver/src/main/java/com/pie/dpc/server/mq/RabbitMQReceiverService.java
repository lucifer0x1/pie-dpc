package com.pie.dpc.server.mq;

import com.pie.common.config.LOCAL_CONSTANTS_CONFIG;
import com.pie.common.notify.MessageObj;
import com.pie.dpc.server.ftp.FtpPropertiesConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/11 15:37
 * @Description TODO :
 **/
@Component
public class RabbitMQReceiverService {

    Logger log = LoggerFactory.getLogger(RabbitMQReceiverService.class);

//    private String queueName = LOCAL_CONSTANTS_CONFIG.DEFAULT_QUEUE_FILE_ARRIVAL;
//    private String exchangeName = LOCAL_CONSTANTS_CONFIG.DEFAULT_EXCHANGE_NAME;
//    private String routeKey = LOCAL_CONSTANTS_CONFIG.DEFAULT_ROUTE_FILE_ARRIVAL;


    @Autowired
    FtpPropertiesConfig config;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = LOCAL_CONSTANTS_CONFIG.DEFAULT_QUEUE_FILE_ARRIVAL,durable = "true",exclusive = "false",autoDelete = "false"),
            exchange = @Exchange(value = LOCAL_CONSTANTS_CONFIG.DEFAULT_EXCHANGE_NAME , type = ExchangeTypes.DIRECT),
            key = LOCAL_CONSTANTS_CONFIG.DEFAULT_ROUTE_FILE_ARRIVAL))
    @RabbitHandler
    public void messageConsume(Message message, Channel channel)  {
        checkFile(message);
    }

    /***
     * TODO 根据消息判断文件是否上传到FTP服务器
     *  真是业务场景： 处理消息，修改文件名，入索引库， 发送下一个环节消息定义
     *
     * @param msg
     */
    private void checkFile(Message msg){
        String ftpBasePath = config.getFtpBasePath();
//        msg.getMessageProperties().getHeaders().forEach((k,v)->{
//            log.debug("k = {} , v = {}",k,v);
//        });
        MessageObj obj = new MessageObj(msg.getMessageProperties().getHeaders());
        File targetFile = new File(ftpBasePath + File.separator + obj.getTargetPath() + File.separator + obj.getFilename());
        if(targetFile.exists()){
            log.info("{} 文件存在 length = {}",targetFile.getAbsoluteFile(),targetFile.length());
        }else {
            log.warn("{} 文件不存在 ",targetFile.getAbsoluteFile());
        }
    }
}
