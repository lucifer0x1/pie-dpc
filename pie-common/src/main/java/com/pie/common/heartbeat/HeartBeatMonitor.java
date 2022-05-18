package com.pie.common.heartbeat;

import com.pie.common.config.LOCAL_CONSTANTS_CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.*;

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


    private ScheduledExecutorService scheduledSender;

    private ScheduledExecutorService scheduledReceiver;


    /**
     * @see HeartBeatType
     * @param type
     * @return
     */
    public static HeartBeatType build(String type){
        return HeartBeatType.valueOf(type);
    }

    public HeartBeatMonitor(){
        init(HeartBeatType.BOTH);
    }

    public HeartBeatMonitor(HeartBeatType type) {
        init(type);
    }

    /**
     * to init Executors
     * @param type
     */
    protected void init(HeartBeatType type){
        switch (type) {
            case SENDER:

                scheduledSender = Executors.newScheduledThreadPool(1, r -> {
                    Thread thread = new Thread(r,"heatbeat sender monitor threadPool");
                    thread.setDaemon(true);
                    return thread;
                });

                break;
            case RECEIVER:
                scheduledReceiver = Executors.newScheduledThreadPool(1,r -> {
                    Thread thread = new Thread(r,"heatbeat receiver monitor threadPool");
                    thread.setDaemon(true);
                    return thread;
                });
                break;
            case BOTH:
            default:
                scheduledSender = Executors.newScheduledThreadPool(1, r -> {
                    Thread thread = new Thread(r,"heatbeat sender monitor threadPool");
                    thread.setDaemon(true);
                    return thread;
                });
                scheduledReceiver = Executors.newScheduledThreadPool(1,r -> {
                    Thread thread = new Thread(r,"heatbeat receiver monitor threadPool");
                    thread.setDaemon(true);
                    return thread;
                });
                break;
        }
    }



    /**
     * 启动发送心跳线程
     * @param TIME_STEP_SECONDS 心跳发送间隔
     */
    public void startSend(long TIME_STEP_SECONDS){
        log.info("Heartbeat Monitor[{}] Starting...","SEND");
        scheduledSender.scheduleAtFixedRate(new SenderHeartBeatThread(this),
        1, TIME_STEP_SECONDS, TimeUnit.SECONDS);

    }

    /**
     * 接收心跳
     * @param TIME_STEP_SECONDS
     */
    public void startCheck(long TIME_STEP_SECONDS){
        log.info("Heart beat Monitor[{}] Starting...","RECV");

        scheduledReceiver.scheduleAtFixedRate(new ReceiverHeartBeatThread(this),
                1, TIME_STEP_SECONDS, TimeUnit.SECONDS);
    }



    /**
     * 心跳检测
     */
    public class ReceiverHeartBeatThread implements  Runnable {

        private HeartBeatMonitor heartBeatMonitor = null;
        private CountDownLatch latch= null;

        public ReceiverHeartBeatThread(HeartBeatMonitor heartBeatMonitor){
            this.heartBeatMonitor = heartBeatMonitor;
            this.latch =  new CountDownLatch(1);
        }



        @Override
        public void run() {
            log.debug("Receiver Check HeartBearMessage");
            RedisTemplate t = null;
            try {
                recvHeartBeatCheck();
            } catch (Exception e){
                log.error("heart beat monitor has  problem {} ,RedisTemplate connection => [{}]" ,
                        e.getMessage() ,t.getClientList().size());
            }

            log.debug("receive check  HeartBearMessage complete");
        }
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
                sendHeartBeat();
            } catch (Exception e){
                log.error("heart beat monitor has  problem {} ,RedisTemplate connection => [{}]" ,
                         e.getMessage() ,t.getClientList().size());
            }

            log.debug("Ending HeartBearMessage");
        }
    }

}

enum HeartBeatType {
    SENDER("this is used by heartbeat sender "),
    RECEIVER("this is used by receive heartbeat to check"),
    BOTH("sender and receiver all running");

    private String description =null;

    HeartBeatType(String desc) {
        description = desc;
    }
}
