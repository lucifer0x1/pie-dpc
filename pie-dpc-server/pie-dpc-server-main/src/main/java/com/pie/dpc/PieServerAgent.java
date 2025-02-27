package com.pie.dpc;

import com.pie.dpc.server.ftp.FtpServerReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/10 18:04
 * @Description TODO : 数据采集加工处理服务端启动模块
 *
 **/
@SpringBootApplication
@ComponentScan({"com.pie.dpc.server.web","com.pie.dpc.server"})
@EnableSwagger2
public class PieServerAgent implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(PieServerAgent.class,args);
    }

    @Autowired
    FtpServerReceiverService ftpService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ftpService.start();
    }
}
