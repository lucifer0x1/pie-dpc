package com.pie.common.ftp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FtpProcessorTest {



    @Autowired
    private FtpProcessor ftpProcessor;


    @Test
    public String test() throws Exception {
        boolean  b1= ftpProcessor.uploadFile("/Inventory", "zw2.txt", "C:/Users/Minco/Desktop/图片/中文.txt");
        boolean  b2= ftpProcessor.uploadFile("/Sellout", "zw3.txt", "C:/Users/Minco/Desktop/图片/中文.txt");
        return "上传结果B1："+b1+",B2:"+b2;
    }

}