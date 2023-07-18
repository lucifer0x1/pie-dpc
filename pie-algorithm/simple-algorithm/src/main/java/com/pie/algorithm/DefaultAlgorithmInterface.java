package com.pie.algorithm;

import com.pie.common.loader.AlgorithmInterface;

public class DefaultAlgorithmInterface implements AlgorithmInterface {

    public DefaultAlgorithmInterface(){
        System.out.println("DefaultAlgorithmInterface Init");
    }

    public String exe( ) {
        System.out.println("无参数  【算法执行】");
        return "无参数 完成执行";
    }


    @Override
    public String exe(Object parameters) {
        System.out.println("【算法执行】 params ==>" + parameters);
        return "完成执行";
    }

    @Override
    public String params() {

        return null;
    }

    public static void main(String[] args) {
        DefaultAlgorithmInterface alg = new DefaultAlgorithmInterface() {

        };
        alg.exe();
    }
}
