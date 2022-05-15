package com.pie.common.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ClassName ReceiverHeartBeartCheckNotify
 * Description
 * Date 2022/5/15
 * Author wangxiyue.xy@163.com
 * @description 检测心跳后 处理方法
 */
public interface ReceiverHeartBeartCheckNotify {

    Logger logger = LoggerFactory.getLogger(ReceiverHeartBeartCheckNotify.class);

    void checkHeartBeat (List<String> heartBeatMessageObjToString);

    default void checkHeartBeat(HeartBeatMessageObj obj){
        logger.error("if you need to check HeatBeart please @Override [checkHeartBeat(HeartBeatMessageObj obj)]");
    };




}
