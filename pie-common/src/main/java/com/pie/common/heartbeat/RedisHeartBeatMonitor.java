package com.pie.common.heartbeat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 通过redis 发送心跳
 */

@Component
public class RedisHeartBeatMonitor extends HeartBeatMonitor {


    /**
     * 心跳间隔时间
     */
    @Value("${heatbeat_step_time_seconds:10}")
    private Long TIME_STEP_SECONDS;

    /**
     * 心跳超时阈值，用户判断心跳超时间隔
     * Redis 中用于心跳信息缓存时间，超过timeout 会被redis抛弃
     * 因此，心跳发送间隔时间(TIME_STEP_SECONDS) 必须小于 (TIME_OUT_SECONDS)
     * redis expirtime
     * @see @TIME_STEP_SECONDS
     */
    @Value("${heatbeat_timeout_seconds:100}")
    private Long TIME_OUT_SECONDS;

    /**
     * 客户端Agent 本地通信IP地址
     *
     * （1） 本地安装部署启动时 指定配置 （springboot 启动参数）
     * （2） 可以从Nacos 识别获取本地（误差较小）
     * （3） 获取本地网卡信息（存在误差较大）
     */
    @Value("${heatbeat_agent_ipaddress:127.0.0.1}")
    private String AGENT_LOCAL_ADDRESS;

    @Value("${redis.key.profix:DATA_COLLECTION}")
    private String REDIS_KEY_PROFIX;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    @PostConstruct
    public void autoStart(){
        log.info("\n AGENT_LOCAL_ADDRESS = {} \n " +
                "TIME_STEP_SECONDS = {} \n " +
                "TIME_OUT_SECONDS ={} \n " +
                "REDIS_KEY_PROFIX ={} \n",
                AGENT_LOCAL_ADDRESS,
                TIME_STEP_SECONDS,
                TIME_OUT_SECONDS,
                REDIS_KEY_PROFIX);

        start(TIME_STEP_SECONDS);
    }


    /**
     * TODO rediskey --> ${profix(projectName)}:${ip}:heartbeat , value --> ${JSON(msg)}
     * @return
     */
    @Override
    public RedisTemplate sendHeartBeat() {
        HeartBeatMessageObj msg = new HeartBeatMessageObj();
        msg.setIpAddress(AGENT_LOCAL_ADDRESS);
        String key =  REDIS_KEY_PROFIX + ":" + msg.getIpAddress() + ":heartbeat";
        redisTemplate.opsForValue().set(key,msg.toString(),TIME_OUT_SECONDS, TimeUnit.SECONDS);
        log.debug(msg.toString());
        return redisTemplate;
    }

    @Override
    public void recvHeartBeatCheck() {
    }
}
