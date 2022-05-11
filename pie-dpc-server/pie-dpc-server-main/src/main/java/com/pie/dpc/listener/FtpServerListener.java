package com.pie.dpc.listener;

import com.pie.dpc.server.ftp.FtpServerReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * ClassName FtpServerListener
 * Description
 * Date 2022/5/10
 * Author wangxiyue.xy@163.com
 */
//@WebListener
@Deprecated
public class FtpServerListener implements ServletContextListener {

    @Autowired
    FtpServerReceiverService ftpService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ftpService.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ftpService.start();
    }
}
