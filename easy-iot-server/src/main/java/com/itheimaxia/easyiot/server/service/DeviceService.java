package com.itheimaxia.easyiot.server.service;

import com.itheimaxia.easyiot.common.bean.Device;
import com.itheimaxia.easyiot.server.bean.DeviceDO;

import java.util.Map;

public interface DeviceService {
    public DeviceDO getDevice(String productName,String deviceName);
    public DeviceDO addDevice(String productName);
    public void webHookHandler(Map webHook);
}
