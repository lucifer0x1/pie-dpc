package com.pie.common.config;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/11 15:43
 * @Description TODO :
 * 本地配置
 **/
public class LOCAL_CONSTANTS_CONFIG {

    public static final String DEFAULT_EXCHANGE_NAME = "DEFAULT_EXCHANGE_PIE_DPC";

    public static final String DEFAULT_QUEUE_FILE_ARRIVAL = "FILE_ARRIVAL_QUEUE";

    public static final String DEFAULT_ROUTE_FILE_ARRIVAL = "FILE_ARRIVAL_ROUTE";

    /**
     * SSHD 连接超时时间 5秒
     */
    public static final long SSHD_NETWORK_CONNECTED_TIME_OUT_SECONDS = 5;

    // 1024 * 1024 * 20= 20mb
    public static final int BYTE_BUFFER_LENGTH = 1024 * 1024 * 20;

    /**
     * agent 安装目录目标目录
     *
     * */
    public static final String AGENT_INSTALL_APP_NAME= "pie-dpc-client-main-1.0-dev.jar";

    public static final String AGENT_INSTALL_ENVIRONMENT_NAME= "jdk1.8.0_321.tar.gz";
    public static final String AGENT_INSTALL_ENVIRONMENT_SUBDIR= "/"+ "jdk1.8.0_321/bin" +"/";

    /**
     * agent 安装包原始路径
     *
     * */
    public static final String AGENT_INSTALL_APP_FULL_PATH = "config" +"/" + AGENT_INSTALL_APP_NAME;

    public static final String AGENT_INSTALL_ENVIRONMENT_FULL_PATH = "config" + "/" +AGENT_INSTALL_ENVIRONMENT_NAME;


}
