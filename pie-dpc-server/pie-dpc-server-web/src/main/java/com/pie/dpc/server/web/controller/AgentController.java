package com.pie.dpc.server.web.controller;

import com.pie.common.heartbeat.HeartBeatMessageObj;
import com.pie.dpc.server.status.HeartBeatCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/17 18:51
 * @Description TODO :
 **/
@Api("服务端查看客户端信息模块")
@RestController
@RequestMapping("/agent")
public class AgentController {


    @RequestMapping(value = "/online",method = RequestMethod.GET)
    @ApiOperation("查看客户端在线状态")
    @ApiResponse(code = 0, message = "ResultOK 中 Data 对象结构", response = HeartBeatMessageObj.class)
    public ResultOK listOnline(){
        List<String>  res = new LinkedList<>();
        HeartBeatCheck.cache.forEach((id,msg)->{
            res.add(msg.toString());
        });

        return ResultOK.ok().setReturnCode(res.size()).setData(res);
    }
}
