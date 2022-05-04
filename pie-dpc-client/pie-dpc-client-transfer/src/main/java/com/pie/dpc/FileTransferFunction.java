package com.pie.dpc;


import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date ${DATE} ${TIME}
 * @Description TODO :
 *
 **/
public interface FileTransferFunction {

    /**
     * 发送文件前调用
     */
    default void beforSendFile(){};

    /**
     * 发送文件后调用，无论发送成功都调用该方法
     */
    default  void afterSendFile() {};

    /**
     * 发送文件流程
     * @param file
     * @return
     */
    boolean sendingFile(File file);

//    boolean sendingFileWithCheck(String fileAndPath,long fileSize);

}
