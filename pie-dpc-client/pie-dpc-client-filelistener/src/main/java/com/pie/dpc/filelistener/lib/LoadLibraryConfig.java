package com.pie.dpc.filelistener.lib;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * ClassName LoadLibraryConfig
 * Description
 * Date 2022/5/9
 * Author wangxiyue.xy@163.com
 */
@Configuration
@Component
public class LoadLibraryConfig {

    static
    {

        System.out.println(LoadLibraryConfig.class.getClassLoader()
                .getResource("lib/libjnotify.so"));

        System.load(LoadLibraryConfig.class.getClassLoader()
                .getResource("lib/libjnotify.so").getPath());

        int res = nativeInit();
        if (res != 0)
        {
            throw new RuntimeException("Error initializing fshook_inotify library. linux error code #" + res  + ", man errno for more info");
        }

    }

    private static native int nativeInit();

}
