package com.pie.dpc.server.web.controller;

import com.pie.dpc.server.web.ResultOK;
import com.pie.dpc.server.web.dao.CollectionDataRecordDao;
import com.pie.dpc.server.web.entity.CollectionDataRecordEntity;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * ClassName CollectionController
 * Description
 * Date 2022/5/21
 * Author wangxiyue.xy@163.com
 * 采集配置
 *
 */
@RestController
@RequestMapping("/collection")
@Api("采集配置")
public class CollectionController {

    Logger log = LoggerFactory.getLogger(CollectionController.class);

    @Autowired
    private CollectionDataRecordDao collectionDataRecordDao;

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @ApiOperation("为🔝客户端添加采集配置记录")
    public ResultOK addDataRecord(@ApiParam(value = "绑定客户端ID ，定时任务为 任务ID",required = true) String clientID ,
                                  @ApiParam(value = "采集数据ID 关联数据定义表",required = true)String dataCode ,
                                  @ApiParam(value = "采集配置目录",required = true) String dataDirectory,
                                  @ApiParam(value = "绑定客户端ID ，定时任务为 任务ID",required = false) String regexStr,
                                  @ApiParam(value = "文件大小范围，文件最 [小] 限制 默认-1 不进行判断",required = false) Long fileSizeMin,
                                  @ApiParam(value = "文件大小范围，文件最 [大] 限制 默认-1 不进行判断",required = false) Long fileSizeMax){

        CollectionDataRecordEntity entity = new CollectionDataRecordEntity(clientID,dataCode,dataDirectory,regexStr,fileSizeMax,fileSizeMin);
        if(entity.getClientID()== null || entity.getClientID().length() <1){
            return ResultOK.fail().setReturnCode(-1).setData("getClientID =" + entity.getClientID());
        }
        if(entity.getDataCode()== null || entity.getDataCode().length() <1){
            return ResultOK.fail().setReturnCode(-2).setData("getDataCode =" + entity.getDataCode());
        }
        if(entity.getDataDirectory()== null || entity.getDataDirectory().length() <1){
            return ResultOK.fail().setReturnCode(-1).setData("getDataDirectory =" + entity.getDataDirectory());
        }
        log.debug("check required param ok {}", clientID);
        if (collectionDataRecordDao.exists(Example.of(entity))) {
            return ResultOK.fail().setReturnCode(0).setData("已经存在 clientID = " + clientID  + " , dataCode = " + dataCode);
        }
        return ResultOK.ok().setReturnCode(0).setData(collectionDataRecordDao.save(entity));
    }


    @RequestMapping(value = "/find",method = RequestMethod.GET)
    @ApiOperation("根据客户端ID 或 任务ID 查询配置采集配置策略")
    public ResultOK findDataRecordByAgentID(@ApiParam(value = "客户端ID 或 任务ID",required = true) String clientID){
        List<CollectionDataRecordEntity>  result  = collectionDataRecordDao.findCollectionDataRecordEntitiesByClientID(clientID);
        if (result.size()>0){
            return ResultOK.ok().setData(result).setReturnCode(result.size());
        }
        return ResultOK.fail().setData(result).setReturnCode(result.size());

    }

}
