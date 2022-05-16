package com.pie.dpc.server;

import com.pie.common.config.LOCAL_CONSTANTS_CONFIG;
import com.pie.dpc.server.status.ConnectSSHServerCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.File;
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
public class InstallAgentExecutor {

    Logger log = LoggerFactory.getLogger(InstallAgentExecutor.class);

    final ConnectSSHServerCheck connect;

    public InstallAgentExecutor(ConnectSSHServerCheck connect) {
        this.connect = connect;
    }

    public boolean install(String path){

        String targetJar = path+ File.separator + LOCAL_CONSTANTS_CONFIG.AGENT_INSTALL_APP_NAME;
        String targetENV = path + File.separator +LOCAL_CONSTANTS_CONFIG.AGENT_INSTALL_ENVIRONMENT_NAME;
        StringBuilder jarInstallStart = new StringBuilder("nohup");
        jarInstallStart.append(targetJar);
        String testJdkVersion = "java -version";

        /**
         * install jdk
         * 如果没有监测到 java 1.8则安装
         * */
        if(!connect.executeCommand(testJdkVersion).contains("java version \"1.8")){
            InputStream jdkInputStream = InstallAgentExecutor.class.getClassLoader()
                    .getResourceAsStream(AGENT_INSTALL_ENVIRONMENT_FULL_PATH);
            if (connect.remoteCopy(jdkInputStream,targetENV)) {
                log.debug("CommandLine Resault: \n {}",connect.executeCommand("tar -zxvf " + targetENV));
                jarInstallStart.append(" ").append(path).append("/jdk/bin/");
            }
        }

        /**
         * install jar  and run jar
          */
        InputStream jarInputStream = InstallAgentExecutor.class.getClassLoader()
                .getResourceAsStream(AGENT_INSTALL_APP_FULL_PATH);
        if (connect.remoteCopy(jarInputStream,targetJar)) {
            jarInstallStart.append("java -jar ").append(targetJar).append(" 2>1 &");
            connect.executeCommand(jarInstallStart.toString());
            return true;
        }

        return false;
    }
}
