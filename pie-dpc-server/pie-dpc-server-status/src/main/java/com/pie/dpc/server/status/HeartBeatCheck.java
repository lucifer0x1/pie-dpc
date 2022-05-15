package com.pie.dpc.server.status;

import com.pie.common.heartbeat.ReceiverHeartBeartCheckNotify;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName HeartBeatCheck
 * Description
 * Date 2022/5/13
 * Author wangxiyue.xy@163.com
 * 心跳检测
 */
@Component
public class HeartBeatCheck implements ReceiverHeartBeartCheckNotify {



    @Override
    public void checkHeartBeat(List<String> heartBeatMessageObjToString) {
        for (String heartBeat : heartBeatMessageObjToString) {
            System.out.println(heartBeat);
        }
    }
}
