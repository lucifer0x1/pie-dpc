package com.pie.dpc.service;

import com.pie.common.collection.CollectionDataRecordObj;
import com.pie.common.ftp.FtpProcessor;
import com.pie.common.notify.MessageObj;
import com.pie.common.notify.MessageType;
import com.pie.dpc.notify.rabbitmq.RabbitMQNotifySender;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName FileAdepterServiceTest
 * Description
 * Date 2022/5/11
 * Author wangxiyue.xy@163.com
 * 测试文件到达后发送消息
 *
 */
@SpringBootTest
class FileAdepterServiceTest {


    FileAdepterService afterFileNotify;

    @Test
    void test(){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!");
    }

    @Autowired
    RabbitMQNotifySender mqSender;

    @Autowired
    FtpProcessor ftpSender;

    @Test
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

    @Test
    void afterFileNotifyFunction() {
        CollectionDataRecordObj recordObj = new CollectionDataRecordObj();
        File file = new File("/Users/lucifer/test/code1/abc.txt");
        recordObj.setDataCode("code1");
        recordObj.setDataCode("/Users");

        afterFileNotify.afterFileNotifyFunction(recordObj,file);

    }

    public static void main(String[] args) {
        System.out.println(StringUtils.capitalize("get"));
    }
}