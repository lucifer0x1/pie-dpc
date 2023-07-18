package com.pie.loader;

/***
 * 异步加载应用实体类
 */
public class ClassFileEntity {
    String fileName;
    String absolutePath;
    String className;
    String description;
    //默认未加载
    STATUS status = STATUS.UNLOAD;

    public ClassFileEntity() {
    }

    public ClassFileEntity(String fileName, String absolutePath, String className, STATUS status) {
        this.fileName = fileName;
        this.absolutePath = absolutePath;
        this.className = className;
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}

enum STATUS {


    UNLOAD("未加载，未运行"),
    LOADED("已加载，未运行"),
    RUNING("已加载，运行中");
    private String desc;

    STATUS(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String contentLine(){
        StringBuilder sb = new StringBuilder("应用加载生命周期 \n");

        sb.append(STATUS.UNLOAD);
        sb.append("(" +STATUS.UNLOAD.desc + ") --->>> ");
        sb.append(STATUS.LOADED);
        sb.append("(" +STATUS.LOADED.desc + ") --->>> ");
        sb.append(STATUS.RUNING);
        sb.append("(" +STATUS.RUNING.desc + ") --->>> ");

        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}