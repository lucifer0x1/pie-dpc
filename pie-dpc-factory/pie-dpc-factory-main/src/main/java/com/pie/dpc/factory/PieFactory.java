package com.pie.dpc.factory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/7/1 13:42
 * @Description TODO :
 **/
@ComponentScan("com.pie")
@SpringBootApplication
public class PieFactory {

    public static void main(String[] args) {
        SpringApplication.run(PieFactory.class,args);
    }

}
