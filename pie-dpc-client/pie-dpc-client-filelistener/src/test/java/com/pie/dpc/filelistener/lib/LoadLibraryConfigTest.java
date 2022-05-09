package com.pie.dpc.filelistener.lib;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassName LoadLibraryConfigTest
 * Description
 * Date 2022/5/9
 * Author wangxiyue.xy@163.com
 */
public class LoadLibraryConfigTest {

    public static void main(String[] args) {
        System.out.println(LoadLibraryConfig.class.getClassLoader().getResource(""));
    }
}