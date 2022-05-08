package com.pie.dpc.filelistener;

import com.pie.common.collection.CollectionDataRecordObj;
import com.pie.dpc.filelistener.strategy.LinuxFileNotifyStrategyService;
import com.pie.dpc.filelistener.strategy.SimpleFileNotifStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("afterFileNotify")
public class FileDirectoryMonitorService {
    Logger log = LoggerFactory.getLogger(FileDirectoryMonitorService.class);



    private final AfterFileNotify afterFileNotify;

    public FileDirectoryMonitorService(AfterFileNotify afterFileNotify) {
        this.afterFileNotify = afterFileNotify;
    }


    public void monitor(CollectionDataRecordObj... path){
        FileNotifyStrategy strategy = null;
        if(isWin32orLinux()){
            strategy = new SimpleFileNotifStrategyService();
        }else {
            strategy = new LinuxFileNotifyStrategyService(afterFileNotify);
        }
        FileNotifyContext context = new FileNotifyContext(strategy);
        context.autoListener(path);
    }

    private boolean isWin32orLinux(){
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            return true;
        }
        //TODO 加载 so



        return false;
    }

}
