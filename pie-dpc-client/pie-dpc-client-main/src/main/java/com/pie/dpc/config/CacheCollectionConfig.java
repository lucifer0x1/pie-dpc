package com.pie.dpc.config;

import com.pie.common.collection.CollectionDataRecordObj;
import com.pie.common.collection.CollectionMessageObj;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/8 23:15
 * @Description TODO : 本地缓存采集配置参数及持久化采集配
 **/
public class CacheCollectionConfig {


    /**
     * key = CollectionDataRecordObj.dataCode
     * val = CollectionDataRecordObj
     */
    public final static ConcurrentHashMap<String, CollectionDataRecordObj> collectionDataRecordCache = new ConcurrentHashMap();

    public void addRecord(CollectionDataRecordObj dataRecordObj) {
        collectionDataRecordCache.put(dataRecordObj.getDataCode(),dataRecordObj);
    }

    public CollectionDataRecordObj getRecordByDataCode(String dataCode){
        return collectionDataRecordCache.get(dataCode);
    }

}
