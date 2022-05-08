package com.pie.dpc.filelistener;

import java.io.File;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/8 22:08
 * @Description TODO :
 **/
public interface AfterFileNotify {

    void afterFileNotifyFunction(File file);
}
