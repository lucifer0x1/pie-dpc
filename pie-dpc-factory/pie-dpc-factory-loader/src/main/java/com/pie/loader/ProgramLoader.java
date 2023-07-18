package com.pie.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.pie.algorithm.DefaultAlgorithmInterface;
import com.pie.common.loader.AlgorithmInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 应用程序加载处理
 *
 */
@Component
public class ProgramLoader {


    Logger log = LoggerFactory.getLogger(ProgramLoader.class);

    /**
     * 全局 动态加载内容汇总
     */
    ConcurrentHashMap<ClassFileEntity,ClassLoader> classLoaders = new ConcurrentHashMap<>();


    /**
     * 通过切面替换
     */
    public void monitor(){

    }

    /**
     *
     * @param absolutePath eg. /opt/data/
     * @param fileName eg. abc.jar
     * @param jarClassName eg. com.pie.loader.ProgramLoader
     *
     */
    public AlgorithmInterface loadByJar(String absolutePath,String fileName,String jarClassName  ){
        URLClassLoader classLoader = null;
        File file  = new File(absolutePath + File.separator + fileName);
        if(!file.exists()){
            log.error("[{}] ===>>> 文件不存在！",file.getAbsolutePath());
            return null;
        }
        if(file.isDirectory()){
            log.error("[{}] ===>>> 文件为目录",file.getAbsolutePath());
            return null;
        }

        try {
            classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            ClassFileEntity entity  = new ClassFileEntity();
            entity.setAbsolutePath(absolutePath);
            entity.setClassName(jarClassName);
            entity.setFileName(fileName);
            entity.setStatus(STATUS.LOADED);

            JarInputStream jarInputStream = new JarInputStream(Files.newInputStream(Paths.get(file.toURI())));
            HashMap<String,int[]> classByteMap = new HashMap<>();
            byte[] buffer = new byte[1024*1024];
            int head = 0 ;
            int chunk = 256;
            JarEntry jarEntry = jarInputStream.getNextJarEntry();
            while (jarEntry!=null){
                String name = jarEntry.getName();
                if(name.endsWith(".class")){
                    String className  = name.replaceAll("/",".")
                            .replaceAll(".class","");
                    int size = 0,read = 0;
                    while (true){
                        read = jarInputStream.read(buffer,head+size ,chunk);
                        if(read == -1) {
                            break;
                        }
                        size +=read;
                    }
                    classByteMap.put(className,new int[]{head,size});
                    head+=size;
                }
                jarEntry = jarInputStream.getNextJarEntry();
            }

            SecureClassLoader secureClassLoader= new SecureClassLoader(){
                @Override
                protected Class<?> findClass(String name){
                    int[] arr = classByteMap.get(name);
                    return super.defineClass(name,buffer,arr[0],arr[1]);
                }
            };
            Class<?> clazz = secureClassLoader.loadClass(DefaultAlgorithmInterface.class.getName());
            Constructor<?> constructor = clazz.getConstructor();
            DefaultAlgorithmInterface entry = (DefaultAlgorithmInterface) constructor.newInstance();

            log.info("info: {} " ,entry.info());
            log.info("params: {} " ,entry.params());
            log.info("exe: {} " ,entry.returnDescrption());
            classLoaders.put(entity,secureClassLoader);
            log.info("loading  [{}]  ===>>> {} , class ===>>> {}" , absolutePath,fileName,jarClassName );

            return entry;

//            String methodName = null;
//            Class parameterTypes = null;
//            Method loadedMethod = loadedClass.getMethod(methodName, parameterTypes);
//            Object parameters = null;
//            Object result = loadedMethod.invoke(loadedClass,parameters) ;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
