package com.pie.common.collection;

import java.io.File;
import java.util.*;

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
        return dataConfigRecords.get(new File(directoryPathStr).getAbsolutePath());
    }

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        for (CollectionDataRecordObj collectionDataRecordObj : dataConfigRecords) {
//            sb.append(collectionDataRecordObj.toString());
//        }
//        return sb.toString();
//    }

    public static void main(String[] args) {
        System.out.println(new File("c:/b/c/d//").getAbsolutePath());
        System.out.println(new File("c:/b/c/d").getAbsolutePath());
    }
}
