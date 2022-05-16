package com.pie.dpc.server.status;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.pie.common.config.LOCAL_CONSTANTS_CONFIG.SSHD_NETWORK_CONNECTED_TIME_OUT_SECONDS;
import static org.apache.sshd.client.SshClient.setUpDefaultClient;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/16 10:03
 * @Description TODO :
 * ssh联通性监测
 **/
@Component
@ComponentScan("con.pie.dpc.server.status")
public class ConnectSSHServerCheck {

    Logger log  =  LoggerFactory.getLogger(ConnectSSHServerCheck.class);


    private final SshClient client;
    private ClientSession clientSession;
    private ConnectFuture connectFuture;


    public ConnectSSHServerCheck(){
        client = setUpDefaultClient();
    }


    /***
     * 测试后销毁
     * TODO must execute after connect
     */
    @PreDestroy
    public void destroy(){
        if(connectFuture!=null && connectFuture.isConnected()){
            connectFuture.cancel();
        }
        if(clientSession!=null){
            clientSession.close(true);
        }
    }

    /**
     * 监测ssh server 链接状态
     * @return
     */
    private boolean connectedState(){
        if(clientSession!=null && clientSession.isOpen() && clientSession.isAuthenticated()){
            return true;
        }
        return false;
    }

    /**
     * 连接后远程执行 linux 命令
     * @param command
     * @return
     */
    public String  executeCommand(String command){
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        String res = "";
        if(connectedState()){
            try {
                res = clientSession.executeRemoteCommand(command,stderr, StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.warn("executeRemoteCommand error => {}",e.getMessage());
            } finally {
                byte[] outBytes = stderr.toByteArray();
                String err =  new String(outBytes, StandardCharsets.UTF_8);
                if(err!=null &&err.length() > 0){
                    log.warn("remote error {}",err);
                }
            }
        } else {
            log.error("you need to connecet SSH server please execute [{}]"
                    ," connect(String username,String password ,String host,int port)");
        }
        return res;
    }


    /**
     * 测试 ssh 链接
     * @param username
     * @param password
     * @param host
     * @param port
     * @return
     */
    public boolean connect(String username,String password ,String host,int port){

        try  {
            client.start();
            connectFuture = client.connect(username, host, port)
                    .verify(SSHD_NETWORK_CONNECTED_TIME_OUT_SECONDS, TimeUnit.SECONDS);
            log.debug("[{}] connect with user [{}]  STATUS => [{}]",host,username,connectFuture.isConnected());

            if(!connectFuture.isConnected()){
                return false;
            }

            clientSession = connectFuture.getSession();
            clientSession.addPasswordIdentity(password);
            AuthFuture verify = clientSession.auth().verify(SSHD_NETWORK_CONNECTED_TIME_OUT_SECONDS, TimeUnit.SECONDS);
            log.debug("[{}] connect with user [{}] PASSWORD  STATUS => [{}]",host,username,verify.isSuccess());

            if(!verify.isSuccess()){
                return false;
            }
            return true;

        }catch ( Exception e){
            log.warn("ssh client error => {}" , e.getMessage());
        }

        return false;
    }
}
