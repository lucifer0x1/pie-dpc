package com.pie.common.ftp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FtpProcessorTest {



    @Autowired
    private FtpProcessor ftpProcessor;


    @Test
    public void test() throws Exception {
        boolean  b1= ftpProcessor.uploadFile("aaa//bbb//ccc/", "1.txt", "/Users/lucifer/test/a.txt");
        System.out.println( "上传结果B1："+b1);
    }

}