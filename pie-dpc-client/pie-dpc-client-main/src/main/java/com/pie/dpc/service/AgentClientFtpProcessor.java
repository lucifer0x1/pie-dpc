package com.pie.dpc.service;

import com.pie.common.ftp.DefaultFtpProcessor;
import com.pie.common.ftp.FtpClientPooledObjectFactory;
import com.pie.common.ftp.FtpProcessor;
import com.pie.common.ftp.FtpProperties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/19 11:10
 * @Description TODO :
 **/
@Component
@Configuration
public class AgentClientFtpProcessor {

    Logger log = LoggerFactory.getLogger(AgentClientFtpProcessor.class);

    @Autowired
    private FtpProperties ftpProperties;

    private ObjectPool<FTPClient> pool;
    private DefaultFtpProcessor ftpProcessor;

    public void restartLoadFtpPool(String ip,String port){
        ftpProperties.setFtpIp(ip);
        ftpProperties.setFtpPort(port);
        log.info("重启！！！ AgentClientFtpProcessor [{}:{}] ",ip,port);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(6000);
        poolConfig.setSoftMinEvictableIdleTimeMillis(50000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);


        log.debug("ftp  : user = {} , pwd = {}",ftpProperties.getFtpUserName(),ftpProperties.getFtpUserPassword());

        ftpProcessor.setHasInit(false);
        pool = new GenericObjectPool<>(new FtpClientPooledObjectFactory(ftpProperties), poolConfig);
        preLoadingFtpClient(ftpProperties.getInitialSize(), poolConfig.getMaxIdle());
        ftpProcessor.setFtpClientPool(pool);
        ftpProcessor.setHasInit(true);
    }

    @Bean
    public FtpProcessor ftpProcessor() {
        log.info("创建 AgentClientFtpProcessor 处理器  [{}:{}]",ftpProperties.getFtpIp(),ftpProperties.getFtpPort());
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(6000);
        poolConfig.setSoftMinEvictableIdleTimeMillis(50000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        pool = new GenericObjectPool<>(new FtpClientPooledObjectFactory(ftpProperties), poolConfig);
        preLoadingFtpClient(ftpProperties.getInitialSize(), poolConfig.getMaxIdle());
        DefaultFtpProcessor processor = new DefaultFtpProcessor(ftpProperties);
        processor.setFtpClientPool(pool);
        processor.setHasInit(true);
        this.ftpProcessor = processor;
        return processor;
    }

    /**
     * 预加载FTPClient连接到对象池中
     *
     * @param initialSize
     * @param maxIdle
     */
    private void preLoadingFtpClient(Integer initialSize, int maxIdle) {
        //如果初始化大小为null或者小于等于0，则不执行逻辑
        if (null == initialSize || initialSize <= 0) {
            return;
        }
        int size = Math.min(initialSize, maxIdle);
        try {
            for (int i = 0; i < size; i++) {
                pool.addObject();
            }
        } catch (Exception e) {
            log.error("预加载失败！", (Object) e.getStackTrace());
        }
    }

}
