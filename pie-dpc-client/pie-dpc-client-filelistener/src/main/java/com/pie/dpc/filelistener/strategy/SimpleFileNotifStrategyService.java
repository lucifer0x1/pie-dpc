package com.pie.dpc.filelistener.strategy;

import com.pie.dpc.filelistener.FileNotifyStrategy;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/3 13:21
 * @Description TODO :
 **/
public class SimpleFileNotifStrategyService  implements FileNotifyStrategy {
    @Override
    public void fileWatch(String filePath) {
        final AtomicLong count = new AtomicLong();
        File dir = new File(filePath);
        if(dir.exists() && dir.isDirectory()){
            FileAlterationObserver observer = new FileAlterationObserver(dir);
            observer.addListener(new FileAlterationListenerAdaptor(){
                @Override
                public void onFileCreate(File file) {
                    System.out.println("文件数["+ count.get() +"] " +"文件创建 --> " + file.getName());
                    count.incrementAndGet();
                }
                @Override
                public void onFileChange(File file) {
                    System.out.println("文件数["+ count.get() +"] " +"文件变化 --> " + file.getName());
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
