package com.pie.dpc;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/2 16:48
 * @Description TODO : FTP 文件传输
 **/
public class FtpTransferDecorator implements FileTransferFunction {




    @Override
    public boolean sendingFile(File file) {


        return false;
    }
}
