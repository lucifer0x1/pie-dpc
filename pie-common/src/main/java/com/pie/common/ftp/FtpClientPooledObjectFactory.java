package com.pie.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/6 01:14
 * @Description TODO :
 **/

public class FtpClientPooledObjectFactory implements PooledObjectFactory<FTPClient> {

    Logger log = LoggerFactory.getLogger(FtpClientPooledObjectFactory.class);

    private FtpProperties ftpProperties;

    public FtpClientPooledObjectFactory(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpProperties.getFtpIp(), Integer.valueOf(ftpProperties.getFtpPort()));
            ftpClient.login(ftpProperties.getFtpUserName(), ftpProperties.getFtpUserPassword());
            log.info("连接ftp服务返回码：" + ftpClient.getReplyCode());
            ftpClient.setBufferSize(ftpProperties.getBufferSize());
            ftpClient.setControlEncoding(ftpProperties.getEncoding());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
//            ftpClient.setActivePortRange(10000,10500);

            return new DefaultPooledObject<>(ftpClient);
        } catch (Exception e) {
            if (ftpClient.isAvailable()) {
                ftpClient.disconnect();
            }
            ftpClient = null;
            log.error("建立ftp连接失败！", (Object) e.getStackTrace());
            throw new Exception("建立ftp连接失败！", e);
        }
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject) throws Exception {
        FTPClient ftpClient = getObject(pooledObject);
        if (null != ftpClient && ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> pooledObject) {
        FTPClient ftpClient = getObject(pooledObject);
        if (null == ftpClient || !ftpClient.isConnected()) {
            return false;
        }
        try {
            ftpClient.changeWorkingDirectory("/");
            return true;
        } catch (IOException e) {
            log.error("验证ftp连接失败！", (Object) e.getStackTrace());
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {

    }

    private FTPClient getObject(PooledObject<FTPClient> pooledObject) {
        if (null == pooledObject || null == pooledObject.getObject()) {
            return null;
        }
        return pooledObject.getObject();
    }
}