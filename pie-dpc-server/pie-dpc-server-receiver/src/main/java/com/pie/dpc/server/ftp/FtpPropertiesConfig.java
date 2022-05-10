package com.pie.dpc.server.ftp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/10 17:18
 * @Description TODO :
 **/



@Component
@PropertySource("classpath:ftpconfig.properties")
@ConfigurationProperties(prefix = "pie.dpc.ftp")
public class FtpPropertiesConfig {

    private String ftpUserName = "ftp";
    private String ftpUserPassword = "ftp";
    private String ftpPort  = "2121";
    private String ftpBasePath = "/data";

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpUserPassword() {
        return ftpUserPassword;
    }

    public void setFtpUserPassword(String ftpUserPassword) {
        this.ftpUserPassword = ftpUserPassword;
    }

    public int getFtpPort() {
        return Integer.valueOf(ftpPort);
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpBasePath() {
        return ftpBasePath;
    }

    public void setFtpBasePath(String ftpBasePath) {
        this.ftpBasePath = ftpBasePath;
    }
}
