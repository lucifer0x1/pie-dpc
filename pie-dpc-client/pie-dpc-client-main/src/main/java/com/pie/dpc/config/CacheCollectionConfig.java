package com.pie.dpc.config;

import com.pie.common.collection.CollectionDataRecordObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/8 23:15
 * @Description TODO : 本地缓存采集配置参数及持久化采集配
 **/
@Configuration
public class CacheCollectionConfig {

    Logger log = LoggerFactory.getLogger(CacheCollectionConfig.class);

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

    public int getRecordSize(){
        return collectionDataRecordCache.size();
    }

    /**
     * 加载采集配置文件
     *
     * propties结构：
     * datacode.dataCode=datacode2
     * datacode.dataDirectory=c\:\\test\\a2
     * datacode.regexStr=*
     * datacode.fileSizeMin=-1
     * datacode.fileSizeMax=-1
     */
    public void loadFromDisk(){
        Properties properties  =new Properties();
        try {
            //TODO 系统默认优先加载 jar包目录下的 config/collection.properties
            String configFilePath  =this.getClass().getClassLoader().getResource("collection.properties").getPath();
            FileInputStream fis = new FileInputStream(configFilePath);
            properties.load(fis);

            Set<String> dataCodes = new HashSet<>();
            properties.forEach((k,v) ->{
                String[] keys = String.valueOf(k).split("\\.");
                dataCodes.add(keys[0]);
            });

            for (String dataCode : dataCodes) {
                CollectionDataRecordObj obj =  new CollectionDataRecordObj();
                obj.setDataCode(dataCode);
                for (Field field : obj.getClass().getDeclaredFields()) {
                    String propName = field.getName();
                    try {
                        Method method = obj.getClass()
                                .getDeclaredMethod("set"+propName.substring(0,1).toUpperCase() +propName.substring(1),null);
                        method.invoke(obj,properties.get(dataCode+"."+ propName ));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        log.warn("dataRecord CLASS warning => [{}]",e.getMessage());
                    }
                }
                addRecord(obj);
            }
            log.debug("load on  success  size ==> [{}]",properties.size());
        } catch (IOException e) {
            log.error("Collection DataRecord Cache Load properties ==> {}",e.getMessage());
        }
    }

    /**
     * 将配置信息写入磁盘
     */
    public void saveOnDisk(){
        Properties properties  =new Properties();
        try {
            collectionDataRecordCache.forEach((dataCode,dataRecordObj) ->{
                for (Field field : dataRecordObj.getClass().getDeclaredFields()) {
                    String propName = field.getName();
                    try {
                        Method method = dataRecordObj.getClass()
                                .getDeclaredMethod("get"+propName.substring(0,1).toUpperCase() +propName.substring(1),null);
                        properties.put(dataCode+"."+ propName , String.valueOf(method.invoke(dataRecordObj)));
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        log.warn("dataRecord CLASS warning => [{}]",e.getMessage());
                    }
                }
            });
            //TODO 系统默认优先加载 jar包目录下的 config/collection.properties
            String configFilePath  =this.getClass().getClassLoader().getResource("collection.properties").getPath();
            OutputStream out = new FileOutputStream(configFilePath);
            properties.store(out,"collection data config rule");
            log.debug("save on  success ==> [{}]",configFilePath);
        } catch (IOException e) {
            log.error("Collection DataRecord Cache save properties ==> {}",e.getMessage());
        }
    }

    /**
     * 将陪孩子信息缓存到Redis
     */
    public void saveOnRedis(){

    }

}
