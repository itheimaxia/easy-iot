package com.itheimaxia.easyiot.common.util;

import com.itheimaxia.easyiot.common.bean.Device;

public final class DeviceUtil {

    public static String getDeviceName(Device device){
        return device.getProduct_name()+"/"+device.getDevice_name();
    }
}
