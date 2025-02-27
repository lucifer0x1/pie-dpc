package com.pie.dpc.filelistener;

import com.pie.common.collection.CollectionDataRecordObj;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.win32.JNotify_win32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/3 10:37
 * @Description TODO :
 **/
public class FileNotifyContext {
    Logger log = LoggerFactory.getLogger(FileNotifyStrategy.class);
    private FileNotifyStrategy notify;
    public static boolean STATUS = false;

    public boolean isSTATUS() {
        return STATUS;
    }

    public void stop(){
        if(STATUS){
            this.notify.cancel();
            STATUS = false;
        }
        log.debug("{} --> notifyStrategy is stop",this.notify.findStrategyName() );
    }

    private CollectionDataRecordObj[] collectionDataRecordObjs;


    public FileNotifyContext(FileNotifyStrategy notifyStrategy) {
        this.notify = notifyStrategy;
    }

    public void autoListener(final String... filePaths){
        Set<String> pathList =  notify.checkPath(filePaths);
        for (String filePath : pathList) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    notify.fileWatch(filePath);
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException e) {
                        log.error("Thread sleep ==> {}",e.getMessage());
                    }
                }
            }).start();
        }
        log.info("[{}] ==> Thread Starting ....",this.notify.findStrategyName());
    }

    public void reListen(){

        if(STATUS){
            this.notify.cancel();
            autoListener(collectionDataRecordObjs);
        } else {
            log.warn("this is not running , please run monitor");
        }
    }

    public void autoListener(final CollectionDataRecordObj... dataRecordObjs){
        STATUS = true;
        this.collectionDataRecordObjs = dataRecordObjs;
//        for (CollectionDataRecordObj dataRecordObj : dataRecordObjs) {
//           dataRecordObj.getDataDirectory();
//        }
        Set<CollectionDataRecordObj> dataRecordSet =  notify.checkPath(dataRecordObjs);

        for (CollectionDataRecordObj recordObj : dataRecordSet) {
            notify.fileWatch(recordObj);
        }
        log.info("[{}] ==> Thread Starting ....",this.notify.findStrategyName());
    }
}
