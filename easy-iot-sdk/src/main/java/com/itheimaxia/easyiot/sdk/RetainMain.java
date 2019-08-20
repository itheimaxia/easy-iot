package com.itheimaxia.easyiot.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RetainMain {
    static Object lock = new Object();
    public static void main(String[] args) {

        IotDevice device = new IotDevice();
//        device.setClientId("mqttjs_e8022a4d0b");
//        device.setHost("tcp://192.168.62.133:1883");
//        device.setProduct_name("xiadong");
//        device.setDevice_name("U2arIT");
//        device.setSecret("TvxY1Q");
        device.connect();
        device.subscribe("test");
        try {
            device.publish("test","aaaaa");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        retain();
    }

    private static void retain(){
        try {
            while(true){
                synchronized(lock){
                    System.out.println( "无限期等待中..." );
                    lock.wait();

                }
            }

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
