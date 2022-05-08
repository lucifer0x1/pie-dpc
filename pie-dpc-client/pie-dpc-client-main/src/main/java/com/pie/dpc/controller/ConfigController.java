package com.pie.dpc.controller;

import com.pie.common.collection.CollectionMessageObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(ConfigController.class);

    @RequestMapping("/recv")
    public ResultOK recvConfig(CollectionMessageObj config){

        return ResultOK.ok().setReturnCode(0).setData(config);
    }
}
