package com.pie.dpc.server.web.controller;

import com.alibaba.fastjson.JSON;
import com.pie.common.heartbeat.HeartBeatMessageObj;
import com.pie.dpc.server.InstallAgentExecutor;
import com.pie.dpc.server.status.HeartBeatCheck;
import com.pie.dpc.server.web.ResultOK;
import com.pie.dpc.server.web.dao.ServerAgentConfigDao;
import com.pie.dpc.server.web.entity.ServerAgentConfigEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
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

    Logger log  = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private InstallAgentExecutor installAgentExecutor;

    @Autowired
    private ServerAgentConfigDao serverAgentConfigDao;

    @RequestMapping(value = "/online",method = RequestMethod.GET)
    @ApiOperation("查看客户端在线状态")
    @ApiResponse(code = 0, message = "ResultOK 中 Data 对象结构", response = HeartBeatMessageObj.class)
    public ResultOK listOnline(){
        List<Object>  res = new LinkedList<>();
        log.debug("from heartbeat find [{}] agent client",HeartBeatCheck.cache.size());
        HeartBeatCheck.cache.forEach((id,msg)->{
            res.add(JSON.parse(msg.toString()));
        });

        return ResultOK.ok().setReturnCode(res.size()).setData(res);
    }


    @RequestMapping(value = "/install",method = RequestMethod.GET)
    @ApiOperation("在线安装客户端")
    @ApiParam
    public ResultOK installAgent(@ApiParam (value = "安装目标服务器登录地址",required = true)String host,
                                 @ApiParam (value = "安装Agent时登录使用的用户",required = true)String user,
                                 @ApiParam (value = "安装Agent时登录使用的密码",required = true)String password ,
                                 @ApiParam (value = "安装目标服务器登录端口",required = true)Integer port ,
                                 @ApiParam (value = "安装Agent软件路径",required =true)String path){
        if(host==null || host.length() ==0){ return ResultOK.fail().setData("param error [host]"); }
        if(user==null || user.length() ==0){ return ResultOK.fail().setData("param error [user]"); }
        if(password==null || password.length() <=0){ return ResultOK.fail().setData("param error [password]"); }
        if(path==null || path.length() ==0){ return ResultOK.fail().setData("param error [path]"); }
        if(port==null || port.intValue()<1){ return ResultOK.fail().setData("param error [port]"); }


        //TODO 保存到数据库
        ServerAgentConfigEntity entity = new ServerAgentConfigEntity();
        entity.setHost(host);
        entity.setPassword(password);
        entity.setUsername(user);
        entity.setPort(port);
        entity.setInstallPath(path);
        serverAgentConfigDao.save(entity);

        if(installAgentExecutor.getConnect().connect(user,password,host,port.intValue())){
            if(installAgentExecutor.install(path)){
                return ResultOK.ok();
            }
        }
        return ResultOK.fail();
    }

    @RequestMapping(value = "/findInstall" ,method = RequestMethod.GET)
    @ApiOperation("获取客户端安装信息")
    public ResultOK findInstall(){
        List<ServerAgentConfigEntity> all = serverAgentConfigDao.findAll();
        return ResultOK.ok().setData(all).setReturnCode(all.size());
    }

    @RequestMapping(value = "/save",method = RequestMethod.GET)
    @ApiOperation("保存客户端目标服务器配置信息")
    public ResultOK saveAgent(@ApiParam (value = "安装目标服务器登录地址",required = true)String host,
                              @ApiParam (value = "安装Agent时登录使用的用户",required = true)String user,
                              @ApiParam (value = "安装Agent时登录使用的密码",required = true)String password ,
                              @ApiParam (value = "安装目标服务器登录端口",required = true)Integer port ,
                              @ApiParam (value = "安装Agent软件路径",required =true)String path){
        if(host==null || host.length() ==0){ return ResultOK.fail().setData("param error [host]"); }
        if(user==null || user.length() ==0){ return ResultOK.fail().setData("param error [user]"); }
        if(password==null || password.length() <=0){ return ResultOK.fail().setData("param error [password]"); }
        if(path==null || path.length() ==0){ return ResultOK.fail().setData("param error [path]"); }
        if(port==null || port.intValue()<1){ return ResultOK.fail().setData("param error [port]"); }


        //TODO 保存到数据库
        ServerAgentConfigEntity entity = new ServerAgentConfigEntity();
        entity.setHost(host);
        entity.setPassword(password);
        entity.setUsername(user);
        entity.setPort(port);
        entity.setInstallPath(path);
        try {
            return  ResultOK.ok().setData(serverAgentConfigDao.save(entity));
        } catch (Exception e){
            log.error("save agent config error to db {}",e.getMessage());
        }
        return ResultOK.fail().setReturnCode(-1);
    }


}
