package com.pie.dpc;


import com.pie.common.collection.CollectionMessageObj;
import com.pie.common.heartbeat.RedisHeartBeatMonitor;
import com.pie.dpc.config.CacheCollectionConfig;
import com.pie.dpc.controller.ConfigController;
import com.pie.dpc.filelistener.FileDirectoryMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@ComponentScan({"com.pie.common","com.pie.dpc"})
public class PieClientAgent implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(PieClientAgent.class, args);
    }

    @Autowired
    FileDirectoryMonitorService monitorService;

    @Autowired
    ConfigController config;

    @Autowired
    CacheCollectionConfig cache;

    @Autowired
    public void run(ApplicationArguments args) {
        cache.loadFromDisk();
        CollectionMessageObj obj = new CollectionMessageObj();
        if(args.containsOption("clientID")){
            for (String clientID : args.getOptionValues("clientID")) {
                obj.setClientID(clientID);
            }
        }
        if(args.containsOption("clientIpAddress")){
            for (String clientIpAddress : args.getOptionValues("clientIpAddress")) {
                obj.setClientIpAddress(clientIpAddress);
            }
        }
        if(args.containsOption("recvIpAddress")){
            for (String recvIpAddress : args.getOptionValues("recvIpAddress")) {
                obj.setRecvIpAddress(recvIpAddress);
            }
        }
        if(args.containsOption("recvPort")){
            for (String recvPort : args.getOptionValues("recvPort")) {
                obj.setRecvPort(recvPort);
            }
        }

        config.installClient(obj);
        monitorService.monitor(cache.getCollectionDataRecordObj());
    }


    @Deprecated
    public void start(ApplicationArguments args)  {

//        installClient

        if(args.containsOption("path")){
            Set<String> paths = new HashSet<>();
            for (String optionValue : args.getOptionValues("path")) {
                paths.add(optionValue);
                System.out.println(optionValue);
            }
            monitorService.monitor(paths.toArray(new String[paths.size()]));
        }else {
            File tmpDir = new File("c:/test");
            if(tmpDir.exists() &&tmpDir.isDirectory() && tmpDir.canRead()){
                /** 此处可以启动默认监听 **/
                monitorService.monitor(tmpDir.getAbsolutePath());
            }else {
                System.out.println(usage());
            }
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String usage(){
        StringBuilder sb = new StringBuilder("\njava -jar ");
        sb.append(PieClientAgent.class.getName())
                .append(" --path=目录1")
                .append(" --path=目录2")
                .append(" --path=目录3 \n");
        return sb.toString();
    };

}
