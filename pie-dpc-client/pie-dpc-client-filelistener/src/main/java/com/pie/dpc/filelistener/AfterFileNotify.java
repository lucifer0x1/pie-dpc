package com.pie.dpc.filelistener;

import com.pie.common.collection.CollectionDataRecordObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/8 22:08
 * @Description TODO :
 *
 **/
public interface AfterFileNotify {

    Logger log = LoggerFactory.getLogger(AfterFileNotify.class);

    default void afterFileNotifyFunction(File file){
        log.warn("if you ues this, you need to @Override afterFileNotifyFunction(File file)");
    };

    default void afterFileNotifyFunction(CollectionDataRecordObj recordObj,File file){
        log.warn("if you ues this, you need to @Override afterFileNotifyFunction(CollectionDataRecordObj recordObj,File file)");
    };
}
