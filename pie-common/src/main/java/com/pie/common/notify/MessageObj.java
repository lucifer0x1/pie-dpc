package com.pie.common.notify;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发现文件后 ，上传服务端消息
 */
public class MessageObj {

    private SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 消息类型
     */
    private MessageType type = MessageType.FILE;
    /**
     * 落地文件名
     */
    private String filename;

    /**
     * 落地文件【相对路径】
     */
    private String targetPath;

    /**
     * 消息返送时间
     */
    private Date sendMsgTime;

    /**
     * 业务_观测数据时间
     * TODO 如果是预报数据 则代表起报时间
     */
    private Date dataTime;

    /**
     * 业务_预报时间（时效）
     * TODO 如果是预报时效，则需要发送时计算时间
     */
    private Date dataForecastTime;

    /**
     * 消息体 业务内容
     */
    private String content;

    @Override
    public String toString() {
        return "MessageObj{" +
                "type=" + type +
                ", filename='" + filename + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", sendMsgTime=" + sendMsgTime +
                ", dataTime=" + dataTime +
                ", dataForecastTime=" + dataForecastTime +
                ", content='" + content + '\'' +
                '}';
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public Date getSendMsgTime() {
        return sendMsgTime;
    }

    public void setSendMsgTime(Date sendMsgTime) {
        this.sendMsgTime = sendMsgTime;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Date getDataForecastTime() {
        return dataForecastTime;
    }

    public void setDataForecastTime(Date dataForecastTime) {
        this.dataForecastTime = dataForecastTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
