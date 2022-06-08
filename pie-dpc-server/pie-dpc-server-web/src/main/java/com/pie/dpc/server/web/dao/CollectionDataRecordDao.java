package com.pie.dpc.server.web.dao;

import com.pie.dpc.server.web.entity.CollectionDataRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ClassName CollectionDataRecordDao
 * Description
 * Date 2022/5/21
 * Author wangxiyue.xy@163.com
 */
public interface CollectionDataRecordDao extends JpaRepository<CollectionDataRecordEntity,Integer> {


    List<CollectionDataRecordEntity> findCollectionDataRecordEntitiesByClientID(String clientID);

}
