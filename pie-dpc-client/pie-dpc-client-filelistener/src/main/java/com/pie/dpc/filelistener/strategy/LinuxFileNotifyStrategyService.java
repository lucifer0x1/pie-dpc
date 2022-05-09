package com.pie.dpc.filelistener.strategy;

import com.pie.common.collection.CollectionDataRecordObj;
import com.pie.dpc.filelistener.AfterFileNotify;
import com.pie.dpc.filelistener.FileNotifyStrategy;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.linux.INotifyListener;
import net.contentobjects.jnotify.linux.JNotify_linux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/3 13:19
 * @Description TODO :
 **/
public class LinuxFileNotifyStrategyService implements FileNotifyStrategy {

    private AfterFileNotify after;
    private int maskDir = JNotify_linux.IN_ISDIR | JNotify_linux.IN_CREATE | JNotify_linux.IN_DELETE | JNotify_linux.IN_IGNORED  ;
    private int maskFile = JNotify_linux.IN_CLOSE_WRITE | JNotify_linux.IN_MOVE ;

    private ConcurrentHashMap<Integer,String> wdTree = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer,CollectionDataRecordObj> recordTreeMap = new ConcurrentHashMap<>();

    public LinuxFileNotifyStrategyService (AfterFileNotify afterEvent){
        this.after = afterEvent;
    }

    @Override
    public void reListen(){

    }

    @Override
    public void fileWatch(CollectionDataRecordObj dataRecord) {
        try {
            wdTree.put( JNotify_linux.addWatch(dataRecord.getDataDirectory(),maskDir),dataRecord.getDataDirectory());

            //TODO 目录下文件变化监听
            int wdFile  = JNotify_linux.addWatch(dataRecord.getDataDirectory(),maskFile);
            recordTreeMap.put(wdFile,dataRecord);


            Set<File> dir = getDirectory(new File(dataRecord.getDataDirectory()));
            log.debug("all dir size => {}",dir.size());
            for (File file : dir) {
                wdTree.put(JNotify_linux.addWatch(file.getAbsolutePath(),maskDir),file.getAbsolutePath());
                //TODO 目录下文件变化监听
                wdFile = JNotify_linux.addWatch(dataRecord.getDataDirectory(),maskFile);
                recordTreeMap.put(wdFile,dataRecord);
            }
            log.debug("wdTree szie == {}",wdTree.size());
            //first listener
            JNotify_linux.setNotifyListener(new LinuxNotifyListener());

        } catch (JNotifyException e) {
            log.error("JNotify error ==> {}", e.getMessage());
        }

    }

    @Override
    public void fileWatch(String filePath) {
        try {

            wdTree.put( JNotify_linux.addWatch(filePath,maskDir),filePath);

            //TODO 目录下文件变化监听
            JNotify_linux.addWatch(filePath,maskFile);

            Set<File> dir = getDirectory(new File(filePath));
            log.debug("all dir size => {}",dir.size());
            for (File file : dir) {
                wdTree.put(JNotify_linux.addWatch(file.getAbsolutePath(),maskDir),file.getAbsolutePath());
                //TODO 目录下文件变化监听
                JNotify_linux.addWatch(filePath,maskFile);
            }
            log.debug("wdTree szie == {}",wdTree.size());

            //first listener
            JNotify_linux.setNotifyListener(new LinuxNotifyListener());
            Thread.sleep(Integer.MAX_VALUE);
        } catch (JNotifyException e) {
            log.error("JNotify error ==> {}",e.getMessage());
        } catch (InterruptedException e) {
            log.error("Thread Sleep Interrupted ==> {}",e.getMessage());
        }
    }

    @Override
    public void afterWatch(CollectionDataRecordObj recordObj,File file) {
        // TODO 发送消息
        if (after != null) {
            after.afterFileNotifyFunction(recordObj,file);
        }
    }

    /**
     * 递归目录
     * @param dir
     * @return 返回全部子目录
     */
    private Set<File> getDirectory(File dir){
        Set<File> subDir = new HashSet<>();
        for (File file : dir.listFiles()) {
            if(!file.canRead()){
                continue;
            }
            if(file.isDirectory()){
                subDir.add(file);
                subDir.addAll(getDirectory(file));
            }
        }
        return subDir;
    }

    @Override
    public String findStrategyName() {
        return "Linux JNotify 监控";
    }

    class LinuxNotifyListener implements INotifyListener {
        Logger log = LoggerFactory.getLogger(LinuxNotifyListener.class);

        @Override
        public void notify(String name, int wd, int mask, int cookie) {
            String fullName = wdTree.get(wd)+ File.separator + name;
            File subDir = new File(fullName);
            log.debug("name = {} , wd = {} , mask = {} , cookie = {} ",
                    fullName,wd,mask,cookie);
            if((mask & maskDir) == mask ){
                //目录变化
                try {
                    if(subDir.exists() && subDir.isDirectory() && subDir.canRead()){
                        // TODO create condition
                        int subWd = JNotify_linux.addWatch(fullName,maskDir);
                        wdTree.put(subWd,fullName);

                        //TODO 目录下文件变化监听
                        int wdFile = JNotify_linux.addWatch(fullName,maskFile);
                        recordTreeMap.put(wdFile,recordTreeMap.get(wd));

                        log.debug("add path[{}]",fullName);
                    }else if (wdTree.containsValue(name)) {
                        // TODO delete condition
                        JNotify_linux.removeWatch(wd);
                        wdTree.remove(wd);
                        log.debug("remove path[{}]",wdTree.get(wd));
                    }
                } catch (JNotifyException e) {
                    log.error("[{}] 目录发生变化",e.getMessage());
                }
            }else  { // TODO if ((mask & maskFile) == mask)
                //文件变化
                afterWatch(recordTreeMap.get(wd),new File(fullName));
                log.debug("file change[{}] wd = {} ,mask = {} , cookie =  {} ",
                        fullName,wd,mask,cookie);
            }
        }
    }
}
