package com.pie.dpc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/7 10:27
 * @Description TODO : 接收服务端 配置信息
 **/
@RestController
@RequestMapping({"/" , "","/api"})
public class ConfigController {

    @RequestMapping("/recv")
    public ResultOK recvConfig(){
        return ResultOK.ok().setReturnCode(0).setData("ssssssss");
    }

}
