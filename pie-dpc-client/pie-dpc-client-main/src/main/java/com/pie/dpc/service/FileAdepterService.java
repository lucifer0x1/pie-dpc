package com.pie.dpc.service;

import com.pie.common.collection.CollectionDataRecordObj;
import com.pie.common.ftp.FtpProcessor;
import com.pie.common.notify.MessageObj;
import com.pie.common.notify.MessageType;
import com.pie.dpc.filelistener.AfterFileNotify;
import com.pie.dpc.notify.rabbitmq.RabbitMQNotifySender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void afterFileNotifyFunction(CollectionDataRecordObj recordObj, File file){
        // if fileSzie enable then check fileSize
        if(recordObj.getFileSizeMin() >0){
            if(file.length()< recordObj.getFileSizeMin()){
                return;
            }
        }
        if(recordObj.getFileSizeMax() >0){
            if(file.length()> recordObj.getFileSizeMax()){
                return;
            }
        }

        boolean isFind = true;
        if(recordObj.getRegexStr()!=null && recordObj.getRegexStr().length()>0){
            //
            Pattern pattern = Pattern.compile(recordObj.getRegexStr());
            Matcher matcher = pattern.matcher(file.getName());
            isFind = matcher.find();
        }
        if(isFind){
            //TODO 发送ftp和消息
            if(ftpSender.uploadFile(recordObj.getDataCode(), file.getName(),file.getAbsolutePath())){
                MessageObj msg = new MessageObj();
                msg.setFilename(file.getName());
                msg.setType(MessageType.FILE);
                msg.setTargetPath(recordObj.getDataCode());
                mqSender.sendNotifyMessage(msg);
            };
        }

    }

}

