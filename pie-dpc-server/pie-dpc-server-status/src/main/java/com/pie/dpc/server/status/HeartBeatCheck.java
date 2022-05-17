package com.pie.dpc.server.status;

import com.pie.common.heartbeat.HeartBeatMessageObj;
import com.pie.common.heartbeat.ReceiverHeartBeartCheckNotify;
import com.pie.common.heartbeat.RedisHeartBeatMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName HeartBeatCheck
 * Description
 * Date 2022/5/13
 * Author wangxiyue.xy@163.com
 * 心跳检测
 */
@Component
@ComponentScan({"com.pie.common"})
@DependsOn("redisHeartBeatMonitor")
public class HeartBeatCheck implements ReceiverHeartBeartCheckNotify {

    public final static ConcurrentHashMap<String, HeartBeatMessageObj> cache = new ConcurrentHashMap<>();

    @Autowired
    RedisHeartBeatMonitor heartBeatMonitor;

    @PostConstruct
    public void  startHeartBeatCheck(){
        heartBeatMonitor.autoStartReceiver(this::checkHeartBeat);
    }

    @Override
    public void checkHeartBeat(List<String> heartBeatMessageObjToString) {
        for (String heartBeat : heartBeatMessageObjToString) {
            HeartBeatMessageObj msg = new HeartBeatMessageObj(heartBeat);
            cache.put(msg.getIpAddress(),msg);
            System.out.println("在线客户端：=>" + heartBeat);
        }
    }
}
