package com.itheimaxia.easyiot.server.controller;

import com.itheimaxia.easyiot.common.bean.Device;
import com.itheimaxia.easyiot.server.bean.DeviceDO;
import com.itheimaxia.easyiot.server.service.DeviceService;
import com.itheimaxia.easyiot.server.service.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/devices/{product_name}")
    public DeviceDO register(@PathVariable("product_name") String productName){
        return deviceService.addDevice(productName);
    }
}
