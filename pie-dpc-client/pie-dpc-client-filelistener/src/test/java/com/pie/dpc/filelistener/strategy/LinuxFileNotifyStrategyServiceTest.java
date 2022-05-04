package com.pie.dpc.filelistener.strategy;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

class LinuxFileNotifyStrategyServiceTest {

    public static void main(String[] args) {
        String path = new String("c:/test/data");
        Set<File> s = getDirectory(new File(path));
        System.out.println("\n");
        for (File file : s) {
            System.out.println(file.getAbsoluteFile());
        }

    }

    public static Set<File> getDirectory(File dir){
        Set<File> subDir = new HashSet<>();
        for (File file : dir.listFiles()) {

            if(!file.canRead()){
                continue;
            }
            if(file.isDirectory()){
                subDir.add(file);
                subDir.addAll(getDirectory(file));
            }
        }
        return subDir;
    }

}