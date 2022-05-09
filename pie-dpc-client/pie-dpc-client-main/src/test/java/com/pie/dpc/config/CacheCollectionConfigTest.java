package com.pie.dpc.config;

import com.pie.common.collection.CollectionDataRecordObj;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheCollectionConfigTest {

    @Autowired
    CacheCollectionConfig cacheConfig;

    @Test
    void saveOnDisk() {
        CollectionDataRecordObj c1 = new CollectionDataRecordObj();
        c1.setDataCode("datacode1");
        c1.setDataDirectory("c:/test/a1");
        c1.setRegexStr("*");

        CollectionDataRecordObj c2 = new CollectionDataRecordObj();
        c2.setDataCode("datacode2");
        c2.setDataDirectory("c:/test/a2");
        c2.setRegexStr("*");

        cacheConfig.addRecord(c1);
        cacheConfig.addRecord(c2);

        cacheConfig.saveOnDisk();
        System.out.println("ok");
    }

    @Test
    void loadFromDisk() {
        System.out.println(cacheConfig.getRecordSize());
        cacheConfig.loadFromDisk();
        System.out.println(cacheConfig.getRecordByDataCode("datacode1").toString());
        System.out.println(cacheConfig.getRecordByDataCode("datacode2").toString());
    }
}