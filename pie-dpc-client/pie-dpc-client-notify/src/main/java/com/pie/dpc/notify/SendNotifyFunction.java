package com.pie.dpc.notify;

import com.pie.common.notify.MessageObj;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/6 23:08
 * @Description TODO :
 *
 * 发送文件到达消息
 **/
public interface SendNotifyFunction {

    /**
     * 发送消息到下一个环节
     * eg：文件到达后发送消息，给文件接收服务端
     * @param notify
     * @return
     */
    boolean sendNotifyMessage(MessageObj notify);
}
