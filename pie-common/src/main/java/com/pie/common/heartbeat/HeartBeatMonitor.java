package com.pie.common.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *  心跳接口
 */
public abstract class HeartBeatMonitor {

    Logger log = LoggerFactory.getLogger(HeartBeatMonitor.class);

    /**
     * 发送心跳方法
     */
    public abstract RedisTemplate sendHeartBeat();

    /**
     * 接受心跳信息,监测是否发送成功
     */
    public  abstract void recvHeartBeatCheck();
    public   final   ScheduledExecutorService scheduledExecutorService;

    public HeartBeatMonitor() {

        scheduledExecutorService = Executors.newScheduledThreadPool(1,new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r,"heatbeat sender monitor threadPool");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    /**
     * 启动发送心跳线程
     */
    public void start(long TIME_STEP_SECONDS){
        log.info("Heartbeat Monitor Starting...");
        scheduledExecutorService.scheduleAtFixedRate(new SenderHeartBeatThread(this),
        1, TIME_STEP_SECONDS, TimeUnit.SECONDS);

    }

    /**
     *  发送心跳线程
     */
   public class SenderHeartBeatThread implements Runnable {

        private HeartBeatMonitor heartBeatMonitor = null;

        public SenderHeartBeatThread( HeartBeatMonitor heartBeatMonitor){
            this.heartBeatMonitor = heartBeatMonitor;
        }

        @Override
        public void run() {
            log.debug("Sending HeartBearMessage");
            RedisTemplate t = null;
            try {
                t  = sendHeartBeat();
                recvHeartBeatCheck();
            } catch (Exception e){
                log.error("heart beat monitor has  problem {} ,RedisTemplate connection => [{}]" ,
                         e.getMessage() ,t.getClientList().size());
            }

            log.debug("Ending HeartBearMessage");
        }
    }

}
