package com.pie.common.collection;

/**
 * @Author wangxiyue.xy@163.com
 * 服务端下发配置收集消息结构
 *
 *
 *
 */
public class CollectionMessageObj {

    /**
     * 安装时分配
     */
    private String clientID;

    /**
     * 客户端IP
     */
    private String clientIpAddress;

    /**
     * 采集数据回传 FTP地址（ip和目录 默认是端IP地址， FTP协议端口默认 2121 ）
     */
    private String recvIpAddress;
    private String recvPort;



}
