package com.pie.common.notify;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 发现文件后 ，上传服务端,同时发送该结构(MessageObj)消息
 */
public class MessageObj {

    private SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");


    private String clientId;

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
     * 消息发送时间
     */
    private Date sendMsgTime = new Date();

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

    public MessageObj(){
        this.sendMsgTime =  new Date();
    }

    public MessageObj(Map<String,Object> param){
        param.forEach((k,v)->{

            String setName = "set"+ StringUtils.capitalize(k);
            try {
                Method method = this.getClass().getDeclaredMethod(setName,v.getClass() );
                method.invoke(this,v);
            } catch (NoSuchMethodException e) {
                System.out.println("can not find funciton => ["+setName+"]   if has param  object=>"+ v.getClass());
            } catch (InvocationTargetException e) {
                System.out.println("can not Invoke => ["+setName+"] ");
            } catch (IllegalAccessException e) {
                System.out.println("can not access  => ["+setName+"] ");
            } catch (Exception e){
                System.out.println(setName +" unknow exception =>" + e.getMessage());
            }
        });

    }

    public MessageObj(MessageType type,
                      String filename,
                      String targetPath,
                      Date dataTime, Date dataForecastTime,
                      String content) {
        this.sendMsgTime = new Date();
        this.type = type;
        this.filename = filename;
        this.targetPath = targetPath;
        this.dataTime = dataTime;
        this.dataForecastTime = dataForecastTime;
        this.content = content;
    }

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

    public Map<String,Object> toMap(){
        HashMap<String,Object> obj = new HashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {
            String getName = "get" +StringUtils.capitalize(field.getName());
            try {
                Method method = this.getClass().getDeclaredMethod(getName);
                obj.put(field.getName(),method.invoke(this));
            } catch (NoSuchMethodException e) {
                System.out.println("can not find funciton => ["+getName+"] ");
            } catch (InvocationTargetException e) {
                System.out.println("can not Invoke => ["+getName+"] ");
            } catch (IllegalAccessException e) {
                System.out.println("can not access  => ["+getName+"] ");
            }
        }
        return obj;
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


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
