package com.pie.common.loader;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.*;

/***
 * 用于自动加载jar包
 *
 */
public class ClassLoader {
    public static void load(String jarPath){
        File jarFile = new File(jarPath);
        if(jarFile.exists()){
            URLConnection urlConnection = null;
            InputStream is =  null;
            try {
                //获取jar 文件url路径
                URL url = jarFile.toURI().toURL();
                //获取系统类加载器
                URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
                //jar路径加入到系统url路径里
                URL pzurl = classLoader.findResource("config.properties");
//                is = classLoader.getResourceAsStream("config.properties");
                urlConnection = pzurl.openConnection();
                Method addObj = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);


                is = urlConnection.getInputStream();
                if(is !=null ){

                    //TODO 读取文件流成功后 操作步骤
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } finally {
                if(is  != null ){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(urlConnection !=null ) {
                    try {
                        ((JarURLConnection) urlConnection).getJarFile().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
