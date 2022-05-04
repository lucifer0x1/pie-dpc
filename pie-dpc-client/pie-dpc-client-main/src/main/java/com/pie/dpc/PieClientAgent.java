package com.pie.dpc;


import com.pie.common.heartbeat.RedisHeartBeatMonitor;
import com.pie.dpc.filelistener.FileDirectoryMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@ComponentScan({"com.pie.common","com.pie.dpc"})
public class PieClientAgent implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(PieClientAgent.class, args);
    }

    @Autowired
    RedisHeartBeatMonitor heartBeatMonitor;

    @Autowired
    FileDirectoryMonitorService monitorService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.containsOption("path")){
            Set<String> paths = new HashSet<>();
            for (String optionValue : args.getOptionValues("path")) {
                paths.add(optionValue);
                System.out.println(optionValue);
            }

            monitorService.monitor(paths.toArray(new String[paths.size()]));
        }else {
            System.out.println(usage());
//            monitorService.monitor("c:\\test\\");
        }

//        FileDirectoryMonitorService listener = new FileDirectoryMonitorService();
//        listener.monitor("c:\\test\\a","c:\\test\\b");
//        monitor.autoStart();
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
