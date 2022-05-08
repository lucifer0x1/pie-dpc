package com.pie.dpc.service;

import com.pie.common.ftp.FtpProcessor;
import com.pie.common.notify.MessageObj;
import com.pie.dpc.filelistener.AfterFileNotify;
import com.pie.dpc.notify.rabbitmq.RabbitMQNotifySender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/8 22:40
 * @Description TODO : 处理文件监听， 通过FTP 传输到服务端后， 发送文件到达消息
 **/
@Component("afterFileNotify")
public class FileAdepterService implements AfterFileNotify {

    @Autowired
    RabbitMQNotifySender mqSender;

    @Autowired
    FtpProcessor ftpSender;



    @Override
    public void afterFileNotifyFunction(File file) {


    }
}

