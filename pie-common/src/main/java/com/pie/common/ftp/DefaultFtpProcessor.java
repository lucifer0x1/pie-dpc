package com.pie.common.ftp;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.pool2.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/6 01:18
 * @Description TODO :
 **/
public class DefaultFtpProcessor implements FtpProcessor {

    Logger log = LoggerFactory.getLogger(DefaultFtpProcessor.class);

    private final FtpProperties ftpProperties;

    //连接池初始化标志
    private boolean hasInit = false;
    //连接池
    private ObjectPool<FTPClient> ftpClientPool;

    public FtpProperties getFtpProperties() {
        return ftpProperties;
    }

    public boolean isHasInit() {
        return hasInit;
    }

    public void setHasInit(boolean hasInit) {
        this.hasInit = hasInit;
    }

    public ObjectPool<FTPClient> getFtpClientPool() {
        return ftpClientPool;
    }

    public void setFtpClientPool(ObjectPool<FTPClient> ftpClientPool) {
        this.ftpClientPool = ftpClientPool;
    }

    public DefaultFtpProcessor(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    /**
     * 上传文件
     *
     * @param path           ftp服务器保存地址
     * @param fileName       上传到ftp的文件名
     * @param originFilePathName 等待上传的文件名（绝对地址或路径）
     */
    @Override
    public boolean uploadFile(String path, String fileName, String originFilePathName) {
        boolean flag = false;
        try {
            InputStream inputStream  = new FileInputStream(originFilePathName);
            flag=uploadFile( path,  fileName,  inputStream);
        } catch (Exception e) {
            log.error("上传文件出错！", (Object) e.getStackTrace());
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param path        ftp服务器保存地址
     * @param fileName    上传到ftp的文件名
     * @param inputStream 文件流
     */
    @Override
    public boolean uploadFile(String path, String fileName, InputStream inputStream) throws IOException {
        boolean flag = false;
        FTPClient ftpClient = getFtpClient();
        try {
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//            createDirectory(path, ftpClient);
//            ftpClient.makeDirectory(path);
//            ftpClient.changeWorkingDirectory(path);
            if(changeAndCreateDirectory(path,ftpClient)){
                ftpClient.changeWorkingDirectory(path);
                flag =ftpClient.storeFile(fileName, inputStream);
            }


        } catch (Exception e) {
            log.error("上传文件出错！:{}", e);
        } finally {
            if(null!=inputStream) inputStream.close();
            releaseFtpClient(ftpClient);
        }
        return flag;
    }

    /**
     * 下载文件
     *
     * @param path      ftp服务器文件路径
     * @param fileName  文件名称
     * @param localPath 下载后的路径
     */
    @Override
    public boolean downloadFile(String path, String fileName, String localPath) {
        boolean flag = false;
        OutputStream outputStream = null;
        FTPClient ftpClient = getFtpClient();
        try {
            ftpClient.changeWorkingDirectory(path);
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (fileName.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localPath + "/" + file.getName());
                    outputStream = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), outputStream);
                    outputStream.close();
                }
            }
            flag = true;
        } catch (IOException e) {
            log.error("下载文件出错！", (Object) e.getStackTrace());
        } finally {
            releaseFtpClient(ftpClient);
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("关闭输出流出错！", (Object) e.getStackTrace());
                }
            }
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param path     ftp文件路径
     * @param fileName 文件名
     */
    @Override
    public boolean deleteFile(String path, String fileName) {
        boolean flag = false;
        FTPClient ftpClient = getFtpClient();
        try {
            ftpClient.changeWorkingDirectory(path);
            ftpClient.dele(fileName);
            ftpClient.logout();
            flag = true;
        } catch (IOException e) {
            log.error("删除文件出错！", (Object) e.getStackTrace());
        } finally {
            releaseFtpClient(ftpClient);
        }
        return flag;
    }

    private static List check(String remote){
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
//            System.out.println("s =" +start + " e =" + end);
        } while (start < remote.length()+1 && end > 0);

        return list;
    }

    public boolean changeAndCreateDirectory (String remote, FTPClient ftpClient)  {
        List<String> remoteDir = check(remote);
        for (String dir : remoteDir) {

            if(!existFile(dir,ftpClient)){
                makeDirectory(dir,ftpClient);
            }
            if(changeWorkingDirectory(dir,ftpClient)){
                log.info("change path ok");
            }
        }
        return true;
    }

    /**
     * 创建多层目录，如果ftp服务器已存在该目录，则不创建，如果没有，则创建
     *
     * @param remote    创建的目录
     * @param ftpClient
     */
    @Override
    public boolean createDirectory(String remote, FTPClient ftpClient) throws IOException {
        String directory = remote + "/";
        //如果远程目录不存在，则递归创建远程目录
        if (!directory.equalsIgnoreCase("/")) {
            int start = 0;
            int end = 0;

            while (start < directory.length()-1){
                if (directory.substring(start,start+1).equals(File.separator)) {
                    start = start +1;
                }else {
                    break;
                }
            }

            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";

            do {
                String subDirectory = new String(remote.substring(start, end).getBytes(ftpProperties.getEncoding()),
                        FtpConstants.DEFAULT_FTP_PATH_ENCODING);
                path = path + "/" + subDirectory;
                if (!existFile(path, ftpClient)) {
                    if (makeDirectory(subDirectory, ftpClient)) {
                        changeWorkingDirectory(subDirectory, ftpClient);
                    } else {
                        log.warn("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory, ftpClient);
                    }
                } else {
                    changeWorkingDirectory(subDirectory, ftpClient);
                }

                start = end + 1;
                while (start < directory.length()-1){
                    if (directory.substring(start,start+1).equals(File.separator)) {
                        start = start +1;
                    }else {
                        break;
                    }
                }
                end = directory.indexOf("/", start);
                log.debug("directory => {} ,start = {} , end = {} " ,directory,start,end);
//                paths = paths + "/" + subDirectory;
            } while (start < end && end > 0);
        }
        return true;
    }

    /**
     * 判断ftp服务器的路径或文件是否存在
     *
     * @param path
     * @param ftpClient
     */
    @Override
    public boolean existFile(String path, FTPClient ftpClient) {
        boolean flag = false;

        FTPFile[] files = new FTPFile[0];
        try {
            files = ftpClient.listFiles(path);
        } catch (IOException e) {
            log.debug("this path [{}]  not on server , exception = {}",path , e.getMessage());
        } finally {
            if (files.length > 0) {
                flag = true;
            }
            log.debug("check path {} exits status = [{}]",path,flag);
            return flag;
        }
    }

    /**
     * 创建目录
     *
     * @param directory
     * @param ftpClient
     */
    @Override
    public boolean makeDirectory(String directory, FTPClient ftpClient) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(directory);
            if (flag) {
                log.info("创建文件夹：" + directory);
            }
        } catch (IOException e) {
            log.error("创建文件夹" + directory + "失败！", (Object) e.getStackTrace());
        }
        return flag;
    }

    /**
     * 切换目录
     *
     * @param directory 要切换的目录
     * @param ftpClient ftp客户端
     */
    public boolean changeWorkingDirectory(String directory, FTPClient ftpClient) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                log.info("进入文件夹：" + directory);
            }
        } catch (IOException e) {
            log.error("进入文件夹："+directory+"错误！", (Object) e.getStackTrace());
        }
        return flag;
    }

    /**
     * 按行读取FTP文件
     *
     * @param remoteFilePath ftp路径
     */
    public List<String> readFileByLine(String remoteFilePath) throws IOException {
        FTPClient ftpClient = getFtpClient();
        try (InputStream inputStream = ftpClient.retrieveFileStream(encodingPath(remoteFilePath));
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .map(line -> StringUtils.trimToEmpty(line))
                    .filter(line -> StringUtils.isNotEmpty(line))
                    .collect(Collectors.toList());
        } finally {
            ftpClient.completePendingCommand();
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 获取指定路径下的ftp文件
     *
     * @param remotePath 指定路径
     */
    public FTPFile[] retrieveFtpFiles(String remotePath) throws IOException {
        FTPClient ftpClient = getFtpClient();
        try {
            return ftpClient.listFiles(encodingPath(remotePath + "/"), file -> file != null && file.getSize() > 0);
        } finally {
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 获取指定ftp路径下的文件名称
     *
     * @param remotePath 指定ftp路径
     */
    @Override
    public List<String> retrieveFileNames(String remotePath) throws IOException {
        FTPFile[] files = retrieveFtpFiles(remotePath);
        if (ArrayUtils.isEmpty(files)) {
            return new ArrayList<>();
        }
        return Arrays.stream(files).filter(Objects::nonNull).map(FTPFile::getName).collect(Collectors.toList());
    }

    /**
     * 获取编码后的文件路径
     */
    private String encodingPath(String path) throws UnsupportedEncodingException {
        //在FTP协议中，规定文件名编码格式为ISO-8859-1，所以目录名或文件名需要转码
        return new String(path.replaceAll("//", "/").getBytes(ftpProperties.getEncoding()), FtpConstants.DEFAULT_FTP_PATH_ENCODING);
    }

    /**
     * 获取ftp客户端
     */
    private FTPClient getFtpClient() {
        checkFtpClientPoolAvailable();
        FTPClient ftpClient = null;
        Exception exception = null;
        //获取连接，做多尝试n次
        try {
            for (int i = 0; i < ftpProperties.getRetryCount(); i++) {
                ftpClient = ftpClientPool.borrowObject();
                ftpClient.enterRemotePassiveMode();
                ftpClient.enterLocalPassiveMode();//设置为被动模式
                ftpClient.changeWorkingDirectory("/");
                break;
            }
        } catch (Exception e) {
            log.error("无法在连接池中获取ftp客户端！", (Object) e.getStackTrace());
            exception = e;
        }
        if (null == ftpClient) {
            throw new RuntimeException("无法在连接池中获取ftp客户端", exception);
        }
        return ftpClient;
    }

    /**
     * 释放ftp客户端
     *
     * @param ftpClient
     */
    private void releaseFtpClient(FTPClient ftpClient) {
        if (null != ftpClient) {
            try {
                //从ftp连接池中移除ftp客户端
                ftpClientPool.returnObject(ftpClient);
            } catch (Exception e) {
                try {
                    //判断客户端是否可用
                    if (ftpClient.isAvailable()) {
                        //销毁连接
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    log.error("销毁ftp连接失败！", (Object) e.getStackTrace());
                }
                log.error("从ftp连接池移除ftp客户端失败！", (Object) e.getStackTrace());
            }
        }
    }

    /**
     * 检查ftp连接池是否可用
     */
    private void checkFtpClientPoolAvailable() {
        Assert.state(hasInit, "ftp未启用或连接失败！");
    }
}
