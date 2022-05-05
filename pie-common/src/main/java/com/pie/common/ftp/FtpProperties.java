package com.pie.common.ftp;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/6 00:52
 * @Description TODO :
 **/
@ConfigurationProperties(prefix = "ftp")
public class FtpProperties {

    private String  ip;
    private String  port;
    private String  username;
    private String  password;
    private Integer initialSize = 0;
    private String  encoding = "UTF-8";
    private Integer bufferSize = 4096;
    private Integer retryCount = 3;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}