package com.pie.dpc.filelistener;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/6/8 16:04
 * @Description TODO :
 **/
public class StringBugTest {

    public static void main(String[] args) {
        String str = "c:\\test\\input\\c:\\test";
        System.out.println(str.replace("c:\\test","code3"));
    }
}
