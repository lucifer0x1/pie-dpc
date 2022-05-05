package com.pie.common.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/5 00:39
 * @Description TODO :
 *
 * 数据目录、采集策略配置记录（一个目录对应一条正则表达式）
 *
 **/
public class CollectionDataRecordObj {

    private String dataCode;
    private String dataDirectory;
    private String regexStr;

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
}
