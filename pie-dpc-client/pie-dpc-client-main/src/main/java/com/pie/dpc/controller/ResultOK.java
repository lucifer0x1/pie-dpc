package com.pie.dpc.controller;

import java.util.concurrent.RecursiveTask;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/7 10:41
 * @Description TODO : 所有页面放回结果包装类
 **/

public class ResultOK<T> {

    //接口调度成功或失败状态
    private boolean returnSuccess = false;

    //自定义返回码
    private int returnCode = 0;

    private T data = null;

    private ResultOK(boolean status) {
        returnSuccess = status;
    }

    public static ResultOK ok(){
        return  new ResultOK(true);
    }

    public static ResultOK fail(){
        return new ResultOK(false);
    }


    public ResultOK setReturnCode(int returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public ResultOK setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isReturnSuccess() {
        return returnSuccess;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public T getData() {
        return data;
    }
}
