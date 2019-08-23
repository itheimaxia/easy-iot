package com.itheimaxia.easyiot.api.util;


import com.itheimaxia.easyiot.api.bean.Device;

public final class DeviceUtil {

    public static String getDeviceName(Device device){
        return device.getProduct_name()+"/"+device.getDevice_name();
    }
}
