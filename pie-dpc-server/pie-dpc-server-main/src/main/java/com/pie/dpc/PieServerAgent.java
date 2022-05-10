package com.pie.dpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/10 18:04
 * @Description TODO : 数据采集加工处理服务端启动模块
 *
 **/
@SpringBootApplication
public class PieServerAgent {

    public static void main(String[] args) {
        SpringApplication.run(PieServerAgent.class,args);
    }

}
