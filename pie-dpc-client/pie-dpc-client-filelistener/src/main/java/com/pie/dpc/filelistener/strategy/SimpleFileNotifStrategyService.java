package com.pie.dpc.filelistener.strategy;

import com.pie.common.collection.CollectionDataRecordObj;
import com.pie.dpc.filelistener.AfterFileNotify;
import com.pie.dpc.filelistener.FileNotifyStrategy;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/3 13:21
 * @Description TODO :
 **/
public class SimpleFileNotifStrategyService  implements FileNotifyStrategy {

    Logger log = LoggerFactory.getLogger(SimpleFileNotifStrategyService.class);

    private AfterFileNotify after;

    public SimpleFileNotifStrategyService(AfterFileNotify afterFileNotify){
        this.after = afterFileNotify;
    }

    private final FileAlterationMonitor monitor = new FileAlterationMonitor();


    @Override
    public void cancel() {
        try {
            monitor.stop();
            log.debug("{} stoped" , this);
        } catch (Exception e) {
            log.warn("FileAlterationMonitor stop error {}",e.getMessage());
        }
    }

    @Override
    public void afterWatch(CollectionDataRecordObj dataRecordObj, File file) {
        // TODO 发送消息
        if (after != null) {
            after.afterFileNotifyFunction(dataRecordObj,file);
        }
    }

    @Override
    public void fileWatch(CollectionDataRecordObj dataRecord) {
        File dir = new File(dataRecord.getDataDirectory());
        if(dir.exists() && dir.isDirectory()){
            FileAlterationObserver observer = new FileAlterationObserver(dir);
            observer.addListener(new FileAlterationListenerAdaptor(){

                @Override
                public void onFileCreate(File file) {
                    log.debug("文件创建 --> {}" , file.getAbsolutePath());
                    afterWatch(dataRecord,file);
                }
                @Override
                public void onFileChange(File file) {
                    log.debug("文件变化 --> {}" , file.getAbsolutePath());
                    // TODO 文件变化可能在 copy 过程中 同一个文件触发多个事件
                    afterWatch(dataRecord,file);
                }
            });
            monitor.addObserver(observer);
            try {
                monitor.start();
                log.debug("Directory Listener on [{}] ",dataRecord.getDataDirectory());
            } catch (Exception e) {
                log.error("FileAlterationMonitor error {}",e.getMessage());
            }
        }else {
            log.warn("[{}]  is not exists or not Dir ",dataRecord.getDataDirectory());
        }


    }

    /**
     * 基于common-io ，监听文件夹，自动监听子目录包括递归事件
     * @param filePath
     */
    @Deprecated
    @Override
    public void fileWatch(String filePath) {
        final AtomicLong count = new AtomicLong();
        File dir = new File(filePath);
        if(dir.exists() && dir.isDirectory()){
            FileAlterationObserver observer = new FileAlterationObserver(dir);
            observer.addListener(new FileAlterationListenerAdaptor(){



                @Override
                public void onFileCreate(File file) {
                    System.out.println("文件数["+ count.get() +"] " +"文件创建 --> " + file.getAbsolutePath());
                    count.incrementAndGet();
                }
                @Override
                public void onFileChange(File file) {
                    System.out.println("文件数["+ count.get() +"] " +"文件变化 --> " + file.getAbsolutePath());
                    count.incrementAndGet();
                }
            });
            FileAlterationMonitor monitor = new FileAlterationMonitor(1000,observer);
            try {
                monitor.start();
                log.debug("Directory Listener on [{}] ",filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("["+filePath+"]  is not exists or not Dir ");
        }
    }

    @Override
    public String findStrategyName() {
        return "Commons-IO 监控";
    }
}
