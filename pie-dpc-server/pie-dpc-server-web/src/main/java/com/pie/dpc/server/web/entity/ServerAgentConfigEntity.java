package com.pie.dpc.server.web.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
@Table(name = "agent_config")
@GenericGenerator(strategy = "uuid",name = "agentInstallClientID")
public class ServerAgentConfigEntity implements Serializable {

    /**
     * 客户端ID
     */
    @Id
    @Column(length = 32)
    @GeneratedValue(generator ="agentInstallClientID" )
    private String clientId;

    /**
     * 客户端显示别名 - 非必填
     */
    @Column(length = 64,nullable = true)
    private String aliasName;

    /***
     * 客户端安装host地址
     *
     */
    @Column(length = 64,nullable = false)
    private String host;

    /**
     * 客户端安装 ssh 登录账号
     *
     */
    @Column(length = 32,nullable = false)
    private String username;


    /**
     * 客户端安装 ssh 登录密码
     */
    @Column(length = 64,nullable = false)
    private String password;

    /***
     * 客户端安装 ssh 登录端口
     *
     */
    @Column(nullable = false)
    private Integer port;

    /***
     * 客户端安装 ssh 安装路径
     */
    @Column(length = 128,nullable = false)
    private String installPath;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

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
