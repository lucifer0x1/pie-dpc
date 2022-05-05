package com.pie.dpc;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/2 16:48
 * @Description TODO : FTP 文件传输
 **/
public class FtpTransferDecorator implements FileTransferFunction {



    public FTPClient getFtpClient(){
        FTPClient ftpClient  = new FTPClient();

        return ftpClient;
    }


    @Override
    public boolean sendingFile(File file) {


        return false;
    }
}
