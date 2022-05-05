package com.pie.common.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.File;
import java.util.*;

/**
 * @Author wangxiyue.xy@163.com
 * 服务端下发配置收集消息结构
 * 配置消息结构
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

    /**
     * 可选参数：用于判断采集的数据是否完整。
     * 文件大小范围，最【大】值，单位 byte
     * 小于 0 无效
     */
    private Long fileSizeMax=-1l;
    /**
     * 可选参数：用于判断采集的数据是否完整。
     * 文件大小范围，最【小】值，单位 byte
     * 小于 0 无效
     */
    private Long fileSizeMin=-1l;
    /**
     * 采集目录配置策略
     *
     * TODO ? 一个目录下 多个产品dataCode 则需要使用Map, 方式，
     */
    private final Map<String,CollectionDataRecordObj> dataConfigRecords =
            new HashMap<String,CollectionDataRecordObj>();

    /**
     * 禁止配置监听重复目录，所以用目录最为key 值
     * @param dataRecordObj
     */
    public  void addDataConfig(CollectionDataRecordObj dataRecordObj){
         dataConfigRecords.put(dataRecordObj.getDataDirectory(),dataRecordObj);
    }

    public CollectionDataRecordObj findDataRecord(String directoryPathStr){
        // 文件路径地址字符转统一格式化
        return dataConfigRecords.get(new File(directoryPathStr).toPath().toString());
    }

    @Override
    public String toString() {
        /**
         * QuoteFieldNames———-输出key时是否使用双引号,默认为true
         * WriteMapNullValue——–是否输出值为null的字段,默认为false
         * WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
         * WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
         * WriteNullStringAsEmpty—字符类型字段如果为null,输出为”“,而非null
         * WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非nul
         */
//        StringBuilder sb = new StringBuilder();
//        for (String keyOfDataDirectory : dataConfigRecords.keySet()) {
//            sb.append(dataConfigRecords.get(keyOfDataDirectory).toString());
//        }
        JSONObject obj = (JSONObject) JSON.toJSON(this);
        obj.put("dataConfigRecords",dataConfigRecords);
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getRecvIpAddress() {
        return recvIpAddress;
    }

    public void setRecvIpAddress(String recvIpAddress) {
        this.recvIpAddress = recvIpAddress;
    }

    public String getRecvPort() {
        return recvPort;
    }

    public void setRecvPort(String recvPort) {
        this.recvPort = recvPort;
    }

    public Long getFileSizeMax() {
        return fileSizeMax;
    }

    public void setFileSizeMax(Long fileSizeMax) {
        this.fileSizeMax = fileSizeMax;
    }

    public Long getFileSizeMin() {
        return fileSizeMin;
    }

    public void setFileSizeMin(Long fileSizeMin) {
        this.fileSizeMin = fileSizeMin;
    }



    public static void main(String[] args) {

        CollectionMessageObj obj = new CollectionMessageObj();
        obj.addDataConfig(new CollectionDataRecordObj(
                "this is data code",
                "this is data directory",
                "this is regex to find file"));
        obj.addDataConfig(new CollectionDataRecordObj(
                "a1",
                "a2/",
                "a3"));
        obj.addDataConfig(new CollectionDataRecordObj(
                "b",
                "a2",
                "b"));
        System.out.println(obj.toString());

    }
}
