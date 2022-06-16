package com.pie.dpc.server.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName MetaDataEntity
 * Description
 * Date 2022/6/11
 * Author wangxiyue.xy@163.com
 * 元数据信息
 */
@Entity
@Table(name = "meta_data")
public class MetaDataEntity implements Serializable {

    /**
     * 元数据ID ， 用于表示[数据]或[产品]的ID信息
     */
    @Id
    @Column(length = 128,nullable = false)
    private String dataCode;

    /**
     * 父ID 信息 ，用于表示目录，如果为null 则表示该数据或产品为 一级目录
     */
    @Column(length = 128)
    private String parentDataCode;

    /**
     * 数据或产品描述信息
     * 有存储 长度限制 200
     */
    @Column(length =200)
    private String description;

    /**
     * 是否为节点：
     * true  表示  datacode
     * false 表示 目录
     */
    @Column(nullable = false)
    private Boolean isNode;

    /**
     * 报错时间，或最后更新时间
     */
    @Column
    private Date saveTime;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getParentDataCode() {
        return parentDataCode;
    }

    public void setParentDataCode(String parentDataCode) {
        this.parentDataCode = parentDataCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public Boolean getNode() {
        return isNode;
    }

    public void setNode(Boolean node) {
        isNode = node;
    }
}
