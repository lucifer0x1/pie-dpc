package com.pie.dpc.server.ftp;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/10 15:34
 * @Description TODO : ftp server
 *
 **/
@Component
public class FtpServerReceiverService {

    private final Logger log = LoggerFactory.getLogger(FtpServerReceiverService.class);
    private FtpServer server;

    @Autowired
    FtpPropertiesConfig ftpConfig;

    public void initFtp(){
        FtpServerFactory serverFactory = new FtpServerFactory();

        /**
         *  ftp connection config
         * */
        DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
        dataConnectionConfigurationFactory.setPassivePorts("10000-10500");
        dataConnectionConfigurationFactory.setActiveEnabled(false);
        /***
         * ftp listener config
         */
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(ftpConfig.getFtpPort());
        listenerFactory.setDataConnectionConfiguration(dataConnectionConfigurationFactory.createDataConnectionConfiguration());
        Listener ftpListener = listenerFactory.createListener();
        /**
         * TODO 此处可以补充 证书加密认证
         *
         */
        serverFactory.addListener("default",ftpListener);



    }

}
