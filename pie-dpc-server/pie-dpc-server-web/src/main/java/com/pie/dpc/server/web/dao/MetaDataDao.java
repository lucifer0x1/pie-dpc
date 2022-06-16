package com.pie.dpc.server.web.dao;

import com.pie.dpc.server.web.entity.MetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ClassName MetaDataDao
 * Description
 * Date 2022/6/11
 * Author wangxiyue.xy@163.com
 */
public interface MetaDataDao extends JpaRepository<MetaDataEntity,String> {

    /**
     * 查询一级目录类标 data
     * @return
     */
    List<MetaDataEntity> findMetaDataEntitiesByParentDataCodeIsNull();

    /***
     * 查询 其他目录  isNode = false 表示目录 ， isNode = true 表示产品数据
     * @param parentDataCode
     * @return
     */

    List<MetaDataEntity> findMetaDataEntitiesByParentDataCodeAndAndIsNodeFalse(String parentDataCode);


    /***
     * 查询 产品或数据 元数据节点 ， isNode = true
     * @return
     */
    List<MetaDataEntity> findMetaDataEntitiesByIsNodeTrue();
}
