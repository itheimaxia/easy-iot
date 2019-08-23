package com.itheimaxia.easyiot.server.service;

import com.itheimaxia.easyiot.server.bean.DeviceDO;
import com.itheimaxia.easyiot.server.entity.PageResult;
import com.itheimaxia.easyiot.server.entity.QueryPageBean;

import java.util.Map;

public interface DeviceService {
    public DeviceDO getDevice(String productName,String deviceName);
    public DeviceDO addDevice(String productName);
    public PageResult findPages(QueryPageBean pageBean);
    public void webHookHandler(Map webHook);
}
