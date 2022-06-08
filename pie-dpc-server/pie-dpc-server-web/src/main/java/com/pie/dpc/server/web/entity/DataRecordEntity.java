package com.pie.dpc.server.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/20 17:12
 * @Description TODO :
 * 数据采集记录配置表
 **/
@Entity
public class DataRecordEntity implements Serializable {

    @Id
    @Column
    private Integer id;
}
