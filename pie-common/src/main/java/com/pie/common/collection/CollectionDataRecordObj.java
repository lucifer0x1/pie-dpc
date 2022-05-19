package com.pie.common.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.File;
import java.io.Serializable;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/5 00:39
 * @Description TODO :
 *
 * 数据目录、采集策略配置记录（一个目录对应一条正则表达式）
 *
 **/
public class CollectionDataRecordObj implements Serializable {


    /**
     *
     */
    private String dataCode;
    private String dataDirectory;
    private String regexStr;

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

    public CollectionDataRecordObj() {
    }

    public CollectionDataRecordObj(String dataCode, String dataDirectory, String regexStr) {
        this.dataCode = dataCode;
        //统一目录格式
        this.dataDirectory =  new File(dataDirectory).toPath().toString();
        this.regexStr = regexStr;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    // SET GET
    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(String dataDirectory) {
        //统一目录格式
        this.dataDirectory = new File(dataDirectory).toPath().toString();
    }

    public String getRegexStr() {
        return regexStr;
    }

    public void setRegexStr(String regexStr) {
        this.regexStr = regexStr;
    }

    public Long getFileSizeMax() {
        return fileSizeMax;
    }

    public void setFileSizeMax(String fileSizeMax) {
        this.fileSizeMax = Long.valueOf(fileSizeMax);
    }

    public Long getFileSizeMin() {
        return fileSizeMin;
    }

    public void setFileSizeMin(String fileSizeMin) {
        this.fileSizeMin = Long.valueOf(fileSizeMin);
    }
}
