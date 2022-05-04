package com.pie.dpc.filelistener.strategy;

import com.pie.dpc.filelistener.FileNotifyStrategy;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.IOException;
import java.nio.file.*;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/3 13:09
 * @Description TODO :
 **/
public class NioFileNotifyStrategyService implements FileNotifyStrategy {


    @Override
    public void fileWatch(String filePath) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(filePath);
            path.register(watchService, new WatchEvent.Kind[] {
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.OVERFLOW
            }, SensitivityWatchEventModifier.HIGH);
            long count = 0;
            while (true){
                try {
                    WatchKey watchKey = watchService.take();
                    for (WatchEvent<?> event: watchKey.pollEvents()) {
                        count ++;
                        //得到 监听的事件类型
                        WatchEvent.Kind kind = event.kind();
                        log.info("[{}]:kind.name = {},",count,kind.name());
                        //得到 监听的文件/目录的路径
                        Path pathName = (Path) event.context();
                        log.info("filename = {} ",pathName.getFileName().toString());
                    }
                    // 每次的到新的事件后，需要重置监听池
                    watchKey.reset();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String findStrategyName() {
        return "JDK Nio 监控";
    }
}
