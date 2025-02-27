package com.pie.dpc.server.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author wangxiyue.xy@163.com
 * @date 2022/5/7 10:41
 * @Description TODO : 所有页面放回结果包装类
 **/
@ApiModel("接口返回包装类")
public class ResultOK {

    @ApiModelProperty("接口调度成功或失败状态")
    private final boolean returnSuccess;

    @ApiModelProperty("自定义返回码")
    private int returnCode = 0;

    @ApiModelProperty("接口返回数据对象")
    private Object data = null;

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

    public ResultOK setData(Object data) {
        this.data = data;
        return this;
    }

    public boolean isReturnSuccess() {
        return returnSuccess;
    }


    @SuppressWarnings("unused")
    public int getReturnCode() {
        return returnCode;
    }

    @SuppressWarnings("unused")
    public Object getData() {
        return data;
    }
}
