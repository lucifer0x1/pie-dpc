package com.pie.common.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/6 01:16
 * @Description TODO :
 **/
public interface FtpProcessor {

    boolean uploadFile(String path, String fileName, String originFileName);

    boolean uploadFile(String path, String fileName, InputStream inputStream) throws IOException;

    boolean downloadFile(String path, String fileName, String localPath);

    boolean deleteFile(String path, String fileName);

    boolean createDirectory(String remote, FTPClient ftpClient) throws IOException;

    boolean existFile(String path, FTPClient ftpClient) throws IOException;

    boolean makeDirectory(String directory, FTPClient ftpClient);

    List<String> retrieveFileNames(String remotePath) throws IOException;



}
