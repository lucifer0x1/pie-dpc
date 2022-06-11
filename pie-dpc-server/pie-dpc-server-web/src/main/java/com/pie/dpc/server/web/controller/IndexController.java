package com.pie.dpc.server.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * ClassName IndexController
 * Description
 * Date 2022/6/11
 * Author wangxiyue.xy@163.com
 */
@RestController
@RequestMapping("")
public class IndexController {


    @RequestMapping("")
    public String index(){
        return "PIE DPC SERVER 0.1 BETA VERSION ===> " + new Date();
    }

    @RequestMapping("/")
    public String alias(){
        return index();
    }
}
