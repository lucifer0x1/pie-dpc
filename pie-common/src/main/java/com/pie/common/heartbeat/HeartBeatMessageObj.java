package com.pie.common.heartbeat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 心跳信息
 */
public class HeartBeatMessageObj {

    private SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 心跳发送时间
     */
    Date sendTime;
    /**
     * 客户端agent IP地址
     */
    String ipAddress;

    /**
     * 客户端ID 唯一序列号，当发生跨网段IP地址相同时同 ID区分
     */
    String clientID;

    /**
     * 客户端agent 监听目录
     */
    String[] listenerDir;

    /**
     * 客户端agent 目录匹配文件正则表达式
     *  目录和表达式一一对应，即：
     *  listenerDir.length = listenerRegex.length
     */
    String[] listenerRegex;

    public String getSendTime() {
        return sdf_yyyyMMddHHmmss.format(sendTime);
    }


    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String[] getListenerDir() {
        return listenerDir;
    }

    public void setListenerDir(String[] listenerDir) {
        this.listenerDir = listenerDir;
    }

    public String[] getListenerRegex() {
        return listenerRegex;
    }

    public void setListenerRegex(String[] listenerRegex) {
        this.listenerRegex = listenerRegex;
    }

    public void setClientID(String clientID){
        this.clientID = clientID;
    };

    public HeartBeatMessageObj() {
        this.sendTime = new Date();
    }

    public HeartBeatMessageObj(String toString){
        JSONObject jsonObject = JSON.parseObject(toString);
        try {
            this.setSendTime(sdf_yyyyMMddHHmmss.parse(String.valueOf( jsonObject.get("sendTime"))));
            this.setIpAddress(String.valueOf(jsonObject.get("ipAddress")));
            this.setClientID(String.valueOf(jsonObject.get("clientID")));
            //TODO 此处数组转换 String 2 arrays 存在性能问题
//            this.setListenerDir(String.valueOf(jsonObject.get("listenerDir")));
//            this.setListenerRegex(String.valueOf(jsonObject.get("listenerRegex")));
        } catch (ParseException e) {
            System.err.println("String to Date : JNON object =>" + jsonObject.get("sendTime").toString());
        }
    }

    public HeartBeatMessageObj(String ipAddress,String clientID, String[] listenerDir, String[] listenerRegex) {
        this.sendTime = new Date();
        this.ipAddress = ipAddress;
        this.clientID = clientID;
        this.listenerDir = listenerDir;
        this.listenerRegex = listenerRegex;
    }

    @Override
    public String toString() {

        return "\n{" +
                " sendTime:" + getSendTime() +
                ", ipAddress:'" + ipAddress + '\'' +
                ", clientID:'" + clientID + '\'' +
                ", listenerDir:" + Arrays.toString(listenerDir) + '\'' +
                ", listenerRegex:" + Arrays.toString(listenerRegex) + '\'' +
                '}';
    }
}
