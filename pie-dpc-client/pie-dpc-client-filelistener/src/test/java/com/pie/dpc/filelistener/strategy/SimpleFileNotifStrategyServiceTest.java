package com.pie.dpc.filelistener.strategy;


class SimpleFileNotifStrategyServiceTest {


    public static void main(String[] args) {
        SimpleFileNotifStrategyService service = new SimpleFileNotifStrategyService(null);
        service.fileWatch("c:\\test");
    }
}