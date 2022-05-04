package com.pie.dpc.filelistener;


import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.linux.JNotify_linux;
import net.contentobjects.jnotify.macosx.JNotify_macosx;
import net.contentobjects.jnotify.win32.JNotify_win32;

import java.util.function.BinaryOperator;

class FileNotifyStrategyTest {

    public static void main(String[] args) {

        int maskDir = JNotify_linux.IN_ISDIR | JNotify_linux.IN_CREATE | JNotify_linux.IN_DELETE;

        Integer a = 32768;

        System.out.println(Integer.toHexString(a));
    }
}