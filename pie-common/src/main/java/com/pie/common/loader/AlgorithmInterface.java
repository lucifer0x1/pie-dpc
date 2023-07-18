package com.pie.common.loader;

public interface AlgorithmInterface<T> {

    /**
     * 算法入口方法
     * @param parameters
     */
    String exe(T parameters);

    /**
     * 算法
     */
    String params();

    /**
     * 算法适用说明
     */
    default String info(){
        return "算法说明 ";
    }

    default String returnDescrption(){
        return "方法返回结果";
    }
}
