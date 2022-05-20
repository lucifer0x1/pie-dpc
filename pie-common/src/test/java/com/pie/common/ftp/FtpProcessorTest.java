package com.pie.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class FtpProcessorTest {



    Logger log = LoggerFactory.getLogger(FtpProcessorTest.class);

    @Autowired
    private FtpProperties ftpProperties;

    @Autowired
    private FtpProcessor ftpProcessor;

    private ObjectPool<FTPClient> pool;

    private DefaultFtpProcessor defaultFtpProcessor;

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

        defaultFtpProcessor.setHasInit(false);

        pool.close();
        pool = new GenericObjectPool<>(new FtpClientPooledObjectFactory(ftpProperties), poolConfig);
        preLoadingFtpClient(ftpProperties.getInitialSize(), poolConfig.getMaxIdle());
        defaultFtpProcessor.setFtpClientPool(pool);
        defaultFtpProcessor.setHasInit(true);
    }



    @Bean
    @PostConstruct
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
        this.defaultFtpProcessor = processor;
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


    @Test
    public void test() throws Exception {
        restartLoadFtpPool("127.0.0.1","2121");

        boolean  b1= ftpProcessor.uploadFile("aaa//bbb//ccc/", "1.txt", "d:/a.txt");
        System.out.println( "上传结果B1："+b1);
    }

    public static void main(String[] args) {
        String s = "aaa/bbbb////cc////dd/dddddd//ee\\ff";
        for (Object o : check(s)) {
            System.out.println(o);
        }
    }

    public static List check(String remote){
        LinkedList list = new LinkedList();
        remote = remote + File.separator;
        int start = 0;
        int end1 = 0;
        int end2 = 0;
        int end = 0;
        String subDir;
        String subChar;

        do {
            //check start head
            while (start < remote.length()-1){
                subChar = remote.substring(start,start+1);
                if (subChar.equals("/")  || subChar.equals("\\")) {
                    start = start +1;

                }else {
                    break;
                }
            }
            subDir = remote.substring(start);

            //check end
            end1 = subDir.indexOf("/");
            end2 = subDir.indexOf("\\");
            if ((end1 >0) && (end2 > 0)) {
                end = Math.min(end1,end2);
            }else {
                end = Math.max(end1,end2);
            }
            if(end< 0){
                break;
            }

            end  = start + end;

            list.add(remote.substring(start,end));
            start = end +1 ;
            System.out.println("s =" +start + " e =" + end);
        } while (start < remote.length()+1 && end > 0);

        return list;
    }

}