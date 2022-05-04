package com.pie.common.notify;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/2 17:03
 * @Description TODO :
 **/
public enum MessageType {

    FILE("文件索引消息","FILE"),
    CONTENT("传输内容","CONTENT"),
    HEARTBEAT("心跳消息:包含配置信息","HEARTBEAT");

    private String description;
    private String value;

    MessageType(String description, String value){
        this.description = description;
        this.value = value;
    }

    public String getValue(){
        return  this.value;
    }

}
