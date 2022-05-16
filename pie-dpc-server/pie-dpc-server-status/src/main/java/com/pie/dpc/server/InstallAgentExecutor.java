package com.pie.dpc.server;

import com.pie.common.config.LOCAL_CONSTANTS_CONFIG;
import com.pie.dpc.server.status.ConnectSSHServerCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import java.io.InputStream;

import static com.pie.common.config.LOCAL_CONSTANTS_CONFIG.*;


/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/16 13:10
 * @Description TODO :
 *
 * 客户端远程安装模块：
 *  （1）远程ssh 登录、
 *  （2）copy 安装包，
 *  （3）执行安装命令
 **/
@Component
@DependsOn("connectSSHServerCheck")
public class InstallAgentExecutor {

    Logger log = LoggerFactory.getLogger(InstallAgentExecutor.class);

    final ConnectSSHServerCheck connect;

    public InstallAgentExecutor(ConnectSSHServerCheck connect) {
        this.connect = connect;
    }

    public ConnectSSHServerCheck getConnect(){
        if(connect==null){
            log.warn("ConnectSSHServerCheck is null");
        }
        return connect;
    }

    public boolean install(String path){

        String targetJar = LOCAL_CONSTANTS_CONFIG.AGENT_INSTALL_APP_NAME;
        String targetENV = LOCAL_CONSTANTS_CONFIG.AGENT_INSTALL_ENVIRONMENT_NAME;
        StringBuilder jarInstallStart = new StringBuilder("nohup");
        String testJdkVersion = "java -version";

        /**
         * install jdk
         * 如果没有监测到 java 1.8则安装
         * */
        if(!connect.executeCommand(testJdkVersion).contains("java version \"1.8")){
            if(!connect.executeCommand(path + AGENT_INSTALL_ENVIRONMENT_SUBDIR + testJdkVersion).contains("java version \"1.8")){
                // 默认安装位置不存在
                InputStream jdkInputStream = InstallAgentExecutor.class.getClassLoader()
                        .getResourceAsStream(AGENT_INSTALL_ENVIRONMENT_FULL_PATH);
                if (connect.remoteCopy(jdkInputStream,path,targetENV)) {
                    log.debug("CommandLine Resault: \n {}",connect.executeCommand("tar -zxvf " + path + "/" + targetENV + " -C " + path));
                    jarInstallStart.append(" ").append(path).append(AGENT_INSTALL_ENVIRONMENT_SUBDIR);
                }else{
                    log.warn("install environment error :{}",path+ "/"+ targetENV);
                    return false;
                }
            }else {
                //agent默认安装位置发现复用java
                log.debug("find jdk last install by agent [{}]",path + "/"+  AGENT_INSTALL_ENVIRONMENT_SUBDIR );
                jarInstallStart.append(" ").append(path).append(AGENT_INSTALL_ENVIRONMENT_SUBDIR);
            }
        }else {
            log.info("find jdk on default []",getConnect().executeCommand("whereis java") );
            jarInstallStart.append(" ");
        }


        /**
         * install jar  and run jar
          */
        InputStream jarInputStream = InstallAgentExecutor.class.getClassLoader()
                .getResourceAsStream(AGENT_INSTALL_APP_FULL_PATH);
        if (connect.remoteCopy(jarInputStream,path,targetJar)) {
            jarInstallStart.append("java -jar ").append(path).append("/").append(targetJar)
                    .append(" > ").append(path).append("/").append("agent.log")
                    .append(" 2>&1  &");
            log.info(connect.executeCommand(jarInstallStart.toString()));
            return true;
        }

        return false;
    }
}
