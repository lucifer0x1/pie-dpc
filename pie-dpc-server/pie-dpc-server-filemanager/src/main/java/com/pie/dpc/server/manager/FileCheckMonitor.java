package com.pie.dpc.server.manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/6/16 15:46
 * @Description TODO :
 * 文件检测根据策，过滤文件，
 **/
@Component
public class FileCheckMonitor {
    Logger log = LoggerFactory.getLogger(FileCheckMonitor.class);

    private ScheduledExecutorService executorService = null;
    private ConcurrentHashMap<String,FileDirCacheStrategy> dirStrategyCache = new ConcurrentHashMap<>();
    volatile boolean isInit = false;

    @Value("${pie.dpc.server.cleanstep:10}")
    private int PIE_DPC_SERVER_CLEANSTEP = 3;


    @PostConstruct
    public void init(){
        executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("Server store file check thread");
                return thread;
            }
        });
        isInit =true;
    }


    /**
     * 取消 定时清理过期文件策略
     * @param path
     */
    public void delCleanStrategy(String path){
        if (dirStrategyCache.containsKey(path)) {
            dirStrategyCache.get(path).future.cancel(false);
            dirStrategyCache.remove(path);
        }else {
            log.warn("[{}] not ont Strategy ",path);
        }
    }

    /**
     * 启动 定时清除过期文件策略
     * @param path
     * @param offset
     * @param unit
     */
    public void addCleanStrategy(String path,Long offset, TimeUnit unit){
        //TODO 生产
//        executorService.scheduleAtFixedRate(new ServerStoreFileCheckLastTimeThread(new File(path),offset,unit)
//        ,1,PIE_DPC_SERVER_CLEANSTEP,TimeUnit.SECONDS);

        // TODO 测试
        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new ServerStoreFileCheckLastTimeThread(new File(path), offset, unit)
                , 1, PIE_DPC_SERVER_CLEANSTEP, TimeUnit.HOURS);

        dirStrategyCache.put(path,new FileDirCacheStrategy(scheduledFuture,offset,unit));
    }

    /**
     * 用于人工立即触发 删除策略以此
     * @param path
     * @param offset
     * @param unit
     */
    public void startOnce(String path,Long offset,TimeUnit unit){
        Thread thread = new Thread(new ServerStoreFileCheckLastTimeThread(new File(path),offset,unit));
        thread.setName("user handle once check thread");
        thread.setDaemon(true);
        thread.start();
    }

}


class FileDirCacheStrategy {

    Long offset;
    TimeUnit unit;
    ScheduledFuture<?> future;
    public FileDirCacheStrategy(ScheduledFuture<?> future,Long offset, TimeUnit unit) {
        this.offset = offset;
        this.unit = unit;
        this.future = future;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }
}




final class ServerStoreFileCheckLastTimeThread implements Runnable {

    Logger log = LoggerFactory.getLogger(ServerStoreFileCheckLastTimeThread.class);

    File filePath;
    Long offsetAge;//时间偏移量
    TimeUnit unit = TimeUnit.DAYS; // 默认按照天

    public ServerStoreFileCheckLastTimeThread(File filePath, Long offsetAge ) {
        this.filePath = filePath;
        this.offsetAge = offsetAge;
        this.unit = TimeUnit.DAYS;
    }

    public ServerStoreFileCheckLastTimeThread(File filePath, Long offsetAge, TimeUnit unit) {
        this.filePath = filePath;
        this.offsetAge = offsetAge;
        this.unit = unit;
    }

    @Override
    public void run() {
        log.debug("Thread Start check......");
        long nowTime = new Date().getTime();
        long offsetAgeMilliSeconds = TimeUnit.MILLISECONDS.convert(offsetAge, unit);
        Long lastTimeAge =  nowTime  - offsetAgeMilliSeconds;
        List<String> oldFiles = checkPath(filePath, lastTimeAge);
        log.info("delete [{}] files ",oldFiles.size());
        log.debug("Thread End check......");
    }

    private List<String> checkPath(File dir , Long lastTimeAge){
        List<String> oldfiles = new LinkedList<>();
        String filePathAndName  = null;
        if(dir!=null && dir.exists() && dir.isDirectory()){
            for (File file : dir.listFiles()) {
                filePathAndName= file.getAbsolutePath();
                if(file.isDirectory()){
                    oldfiles.addAll(checkPath(file,lastTimeAge));
                }else {
                    if(file.lastModified() <lastTimeAge){
                        if (file.delete()) {
                            oldfiles.add(filePathAndName);
                            log.debug("Delete for Find Old File [{}] ",filePathAndName);
                        }else {
                            log.debug("can not delete [{}] ",filePathAndName);
                        }

                    }
                    log.debug("Time live ok : {}",filePathAndName);
                }
            }
        }
        return oldfiles;
    }
}
