package com.pie.dpc.server.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/18 17:02
 * @Description TODO :
 *
 * Agent采集目标服务器配置信息
 *
 **/
@Entity
public class ServerAgentConfigEntity implements Serializable {

    @Id
    @Column
    private String clientId;

    @Column
    private String host;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private Integer port;

    @Column
    private String installPath;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }
}
