package com.pie.dpc.filelistener.strategy;

import com.pie.dpc.filelistener.FileNotifyStrategy;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.win32.IWin32NotifyListener;
import net.contentobjects.jnotify.win32.JNotify_win32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/3 13:16
 * @Description TODO :
 **/
public class Win32FileNotifyStrategyService implements FileNotifyStrategy {

    @Override
    public void fileWatch(String filePath) {
        int mask = JNotify_win32.FILE_NOTIFY_CHANGE_CREATION ;
        try {
            int wd = JNotify_win32.addWatch(filePath,mask,true);
            JNotify_win32.setNotifyListener(new Win32NotifyListener());
            Thread.sleep(Integer.MAX_VALUE);
        } catch (JNotifyException e) {
            log.error("JNotify error ==> {}",e.getMessage());
        } catch (InterruptedException e) {
            log.error("Thread Sleep Interrupted ==> {}",e.getMessage());
        }
    }

    @Override
    public String findStrategyName() {
        return "Windows JNotify 监控";
    }

    static class Win32NotifyListener implements IWin32NotifyListener {
        Logger log = LoggerFactory.getLogger(IWin32NotifyListener.class);
        @Override
        public void notifyChange(int wd, int action, String rootPath, String filePath) {
            log.info("[action = {}] wd = {} ,rootPath = {} , filePath =  {} ",
                    action,wd,rootPath,filePath);
        }
    }
}
