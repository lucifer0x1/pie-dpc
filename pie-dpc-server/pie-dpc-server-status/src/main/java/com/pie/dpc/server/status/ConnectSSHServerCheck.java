package com.pie.dpc.server.status;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.threads.SshThreadPoolExecutor;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static com.pie.common.config.LOCAL_CONSTANTS_CONFIG.BYTE_BUFFER_LENGTH;
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
    private SftpClient sftpClient;



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

        if(client!=null && client.isStarted()){
            client.stop();
        }
    }

    /**
     * 监测ssh server 链接状态
     * @return
     */
    private boolean connectedState(){
        if(clientSession!=null &&
                clientSession.isOpen() &&
                clientSession.isAuthenticated() &&
                sftpClient!=null && sftpClient.isOpen()){
            return true;
        }
        return false;
    }

    public boolean remoteCopy(InputStream fis, String dstFileFullPath){
        if(connectedState()){
            try {
                SftpClient.CloseableHandle handle = sftpClient.open(dstFileFullPath,
                        EnumSet.of(SftpClient.OpenMode.Write, SftpClient.OpenMode.Create));
                byte[] buff = new byte[BYTE_BUFFER_LENGTH];
                int readLength  = fis.read(buff);
                long fileOffset = 0l;

                while (readLength!=-1){
                    sftpClient.write(handle,fileOffset,buff,0,readLength);
                    fileOffset++;
                    readLength = fis.read(buff);
                }

                log.debug("InputStream upload to [{}] success upload size = {}",dstFileFullPath,fileOffset);
                return true;

            } catch (IOException e) {
                log.warn("sftp open handle error [{}]",e.getMessage());
                e.printStackTrace();
            }
        }
        log.debug("sshd(or sftp) connect error");
        return  false;
    }

    public boolean remoteCopy(String srcFileFullPath,String dstFileFullPath){
        if(connectedState()){
            try {
                SftpClient.CloseableHandle handle = sftpClient.open(dstFileFullPath,
                        EnumSet.of(SftpClient.OpenMode.Write, SftpClient.OpenMode.Create));
                File srcFile = new File(srcFileFullPath);
                if(srcFile.exists() &&srcFile.canRead()){
                    byte[] buff = new byte[BYTE_BUFFER_LENGTH];
                    FileInputStream fis = new FileInputStream(srcFile);
                    int readLength  = fis.read(buff);
                    long fileOffset = 0l;

                    while (readLength!=-1){
                        sftpClient.write(handle,fileOffset,buff,0,readLength);
                        fileOffset++;
                        readLength = fis.read(buff);
                    }

                    log.debug("file[{}] upload to [{}] success upload size = {}",srcFileFullPath,dstFileFullPath,fileOffset);
                    return true;
                }else {
                    log.warn("src[{}] not exists or can not read ",srcFile.getAbsoluteFile());
                    return false;
                }

            } catch (IOException e) {
                log.warn("sftp open handle error [{}]",e.getMessage());
                e.printStackTrace();
            }
        }
        log.debug("sshd(or sftp) connect error");
        return  false;
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

            /**
             * 来接成功后，可以创建sftp client
             **/
            sftpClient = SftpClientFactory.instance().createSftpClient(clientSession);

            return true;

        }catch ( Exception e){
            log.warn("ssh client error => {}" , e.getMessage());
        }

        return false;
    }
}
