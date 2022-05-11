package com.pie.dpc.server.ftp;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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

    public FtpServerReceiverService () {

    }

    @Autowired
    FtpPropertiesConfig ftpConfig;

    public void stop(){
        if(server!=null){
            server.stop();
        }
        log.debug("ftp server stop!!!");
    }

    public void start(){
        try {
            if(server== null){
                server = initFtp();
            }
            if(server.isStopped()){
                server.start();
            }
            if(server.isSuspended()){
                server.resume();
            }
        } catch (FtpException e) {
            log.error("ftp server start not success");
        }
        log.info("ftp server starting on [{}]",ftpConfig.getFtpPort());
    }

    private void  setLogLevel(){
        log.debug("set ftp log level = > {}" ,Level.SEVERE);
        java.util.logging.Logger javaUtilLogging = java.util.logging.Logger.getLogger("org.apache.ftpserver.listener.nio.FtpLoggingFilter");
        javaUtilLogging.setLevel(Level.SEVERE);
    }

    private FtpServer initFtp(){

        FtpServerFactory serverFactory = new FtpServerFactory();
        /**
         *  ftp connection config
         * */
        DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
        dataConnectionConfigurationFactory.setPassivePorts("10000-10500");
        dataConnectionConfigurationFactory.setActiveEnabled(false);
        log.debug("create data connection config factory");
        /***
         * ftp listener config
         */
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(ftpConfig.getFtpPort());
        listenerFactory.setDataConnectionConfiguration(dataConnectionConfigurationFactory.createDataConnectionConfiguration());
        Listener ftpListener = listenerFactory.createListener();
        serverFactory.addListener("default",ftpListener);
        log.debug("add listener factory");
        /**
         * TODO 此处可以补充 证书加密认证
         *
         */

        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(new WritePermission());
        log.debug("create user Authority List ");
        BaseUser user = new BaseUser();
        user.setEnabled(true);
        user.setName(ftpConfig.getFtpUserName());
        user.setPassword(ftpConfig.getFtpUserPassword());
        user.setHomeDirectory(ftpConfig.getFtpBasePath());
        user.setMaxIdleTime(0);
        user.setAuthorities(authorityList);
        log.debug("create ftp user ");
        /**
         * user manager
         */
        UserManager userManager = serverFactory.getUserManager();
        try {
            userManager.save(user);
        } catch (FtpException e) {
            log.error("UserManager save [{}] not success",user.getName());
        }
        serverFactory.setUserManager(userManager);
        log.debug("add UserManager success");
        return serverFactory.createServer();
    }

}
