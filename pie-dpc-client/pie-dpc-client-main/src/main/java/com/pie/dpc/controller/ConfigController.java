package com.pie.dpc.controller;

import com.pie.common.collection.CollectionDataRecordObj;
import com.pie.common.collection.CollectionMessageObj;
import com.pie.dpc.config.CacheCollectionConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/7 10:27
 * @Description TODO : 接收服务端 配置信息
 **/
@RestController
@RequestMapping({"/" , "","/api"})
@Api("客户端接收配置")
public class ConfigController {

    Logger log = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    CacheCollectionConfig cacheConfig;

    @RequestMapping(value = "/recv",method = RequestMethod.GET)
    @ApiOperation("配置采集目录")
    public ResultOK recvConfig(CollectionDataRecordObj config){
        cacheConfig.addRecord(config);
        return ResultOK.ok().setReturnCode(0).setData(config);
    }
}
