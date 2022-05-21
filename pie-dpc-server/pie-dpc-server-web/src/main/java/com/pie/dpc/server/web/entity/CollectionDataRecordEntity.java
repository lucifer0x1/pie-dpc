package com.pie.dpc.server.web.entity;

import org.hibernate.id.UUIDHexGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ClassName CollectionDataRecordEntity
 * Description
 * Date 2022/5/21
 * Author wangxiyue.xy@163.com
 * 目录监听采集配置
 */
@Entity
@Table(name = "data_record")
@IdClass(CollectionDataRecordEntityPK.class)
public class CollectionDataRecordEntity implements Serializable {


    /**
     * 采集配置关联客户端ID ，用于区分该采集配合所属客户端
     * 如果为空 则关联 采集定时任务ID
     */
    @Id
    @Column(nullable = false,length = 100)
    private String clientID;

    /**
     * 资料编码，关联资料编码表
     * data + client 是主键
     */
    @Id
    @Column(nullable = false,length = 100)
    private String dataCode;
    /**
     * 采集目录地址，绝对路径
     */
    @Column(nullable = false,length = 128)
    private String dataDirectory;

    /**
     * 采集文件正则表达式
     */
    @Column(nullable = true,length = 255)
    private String regexStr;

    /**
     * 可选参数：用于判断采集的数据是否完整。
     * 文件大小范围，最【大】值，单位 byte
     * 小于 0 无效
     */
    @Column
    private Long fileSizeMax=-1l;
    /**
     * 可选参数：用于判断采集的数据是否完整。
     * 文件大小范围，最【小】值，单位 byte
     * 小于 0 无效
     */
    @Column
    private Long fileSizeMin=-1l;

    public CollectionDataRecordEntity() {
    }

    public CollectionDataRecordEntity(String clientID, String dataCode, String dataDirectory, String regexStr, Long fileSizeMax, Long fileSizeMin) {
        this.clientID = clientID;
        this.dataCode = dataCode;
        this.dataDirectory = dataDirectory;
        this.regexStr = regexStr;
        this.fileSizeMax = fileSizeMax;
        this.fileSizeMin = fileSizeMin;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

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
        this.dataDirectory = dataDirectory;
    }

    public String getRegexStr() {
        return regexStr;
    }

    public void setRegexStr(String regexStr) {
        this.regexStr = regexStr;
    }

    public Long getFileSizeMax() {
        return fileSizeMax;
    }

    public void setFileSizeMax(Long fileSizeMax) {
        this.fileSizeMax = fileSizeMax;
    }

    public Long getFileSizeMin() {
        return fileSizeMin;
    }

    public void setFileSizeMin(Long fileSizeMin) {
        this.fileSizeMin = fileSizeMin;
    }
}

class CollectionDataRecordEntityPK implements Serializable {
    @Id
    @Column(nullable = false,length = 100)
    private String clientID;

    /**
     * 资料编码，关联资料编码表
     * data + client 是主键
     */
    @Id
    @Column(nullable = false,length = 100)
    private String dataCode;
}

