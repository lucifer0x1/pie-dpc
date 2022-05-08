package com.pie.dpc.filelistener;

import com.pie.common.collection.CollectionDataRecordObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/3 10:00
 * @Description TODO :
 * 目录监听 （策略模式Strategy）
 **/
public interface FileNotifyStrategy {

    Logger log = LoggerFactory.getLogger(FileNotifyStrategy.class);

    default Set<CollectionDataRecordObj> checkPath(CollectionDataRecordObj dataRecordObj){
        log.warn("if you use this function ,you need to @Override [checkPath(CollectionDataRecordObj dataRecordObj)]");
        return null;
    };

    /**
     * 判断是否可被监听
     * @param filePath
     * @return
     */
    default Set<String> checkPath(String... filePath){
        Set<String> checkOk = new HashSet<>();
        File dir = null;
        for (String path : filePath) {
            dir = new File(path);
            if(dir.exists() && dir.isDirectory() && dir.canRead()){
                checkOk.add(path);
            }else {
                log.error("[{}] ==> [exists = {}],[siDir = {}],[canRead = {}]",
                        filePath,dir.exists(),dir.isDirectory(),dir.canRead());
            }
        }
        if(checkOk.size() <=0){
            log.error("There is no Directory to Listener !!!");
        }
        return checkOk;
    };

    default void fileWatch(CollectionDataRecordObj dataRecord) {
        log.warn("if you use this function ,you need to @Override [fileWatch(CollectionDataRecordObj dataRecord)]");
    };

    /**
     * 文件监听 逻辑（各种监听手段策略， 这些策略选用一种方式）
     * @param filePath
     */
    void fileWatch(String filePath);

    /**
     * 文件变化发生后，处理该文件
     * @param targetFile
     */
    default void afterWatch(File targetFile) {
        log.debug("新文件落地[{}] size = {},lastTime = {}",
                targetFile.getAbsoluteFile(),targetFile.length());
    };


    /**
     * 监听文件变化，工作模式。
     * @return
     */
    String findStrategyName();

}
