package com.pie.dpc.server.web.controller;

import com.pie.dpc.server.web.ResultOK;
import com.pie.dpc.server.web.dao.MetaDataDao;
import com.pie.dpc.server.web.entity.MetaDataEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * ClassName MetaDataController
 * Description
 * Date 2022/6/11
 * Author wangxiyue.xy@163.com
 * 元数据管理 - 采集编码配置， 目录分类
 */
@Api("元数据管理，采集编码配置，目录分类等")
@RestController
@RequestMapping("/meta")
public class MetaDataController {

    @Autowired
    private MetaDataDao metaDataDao;

    @RequestMapping("/header")
    @ApiOperation("获取数据分类")
    public ResultOK listHeader(){
        List<MetaDataEntity> metaDatas = metaDataDao.findMetaDataEntitiesByParentDataCodeIsNull();
        if(metaDatas.size()>0){
            return ResultOK.ok().setReturnCode(metaDatas.size()).setData(metaDatas);
        }
        return ResultOK.fail().setData("can not find metaData");
    }

    @RequestMapping("/findMenu")
    @ApiOperation("根据 dataCode 获取其他目录")
    public ResultOK findDataByParent(@ApiParam(value = "上级目录dataCode") String dataCode){
        List<MetaDataEntity> metaDatas = metaDataDao.findMetaDataEntitiesByParentDataCodeAndAndIsNodeFalse(dataCode);
        if(metaDatas.size()>0){
            return ResultOK.ok().setReturnCode(metaDatas.size()).setData(metaDatas);
        }
        return ResultOK.fail();
    }

    @RequestMapping("/listNode")
    @ApiOperation("获取全部非目录 datacode 即 产品数据标识")
    public ResultOK findAllDataNode(){
        List<MetaDataEntity> metaDatas = metaDataDao.findMetaDataEntitiesByIsNodeTrue();
        if(metaDatas.size()>0){
            return ResultOK.ok().setReturnCode(metaDatas.size()).setData(metaDatas);
        }
        return ResultOK.fail();
    }

    @RequestMapping("/saveMetaData")
    @ApiOperation("保存元数据信息")
    public ResultOK saveMetaData(@ApiParam(value = "上级目录dataCode") String parentDataCode,
                             @ApiParam(value = "目录dataCode 或 数据产品标识",required = true) String dataCode,
                             @ApiParam(value = "目录或数据产品 描述信息")String description,
                             @ApiParam(value = "false 表示目录，true 表示数据产品",required = true) Boolean isNode){
        if(dataCode==null || dataCode.length() ==0){ return ResultOK.fail().setData("param error [dataCode]"); }
        MetaDataEntity entity = new MetaDataEntity();
        entity.setDataCode(dataCode);
        entity.setNode(isNode);
        entity.setDescription(description);
        entity.setSaveTime(new Date());
        entity.setParentDataCode(parentDataCode);
        MetaDataEntity save = metaDataDao.save(entity);
        if(save!=null){
          return ResultOK.ok().setData(save);
        }
        return ResultOK.fail();
    }

}
