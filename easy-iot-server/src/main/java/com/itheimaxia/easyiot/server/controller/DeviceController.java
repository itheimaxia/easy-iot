package com.itheimaxia.easyiot.server.controller;

import com.itheimaxia.easyiot.common.bean.Device;
import com.itheimaxia.easyiot.server.bean.DeviceDO;
import com.itheimaxia.easyiot.server.service.DeviceService;
import com.itheimaxia.easyiot.server.service.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/devices/{product_name}/{device_name}")
    public DeviceDO register(@PathVariable("product_name") String productName,
                             @PathVariable("device_name") String deviceName){
        return deviceService.getDevice(productName,deviceName);
    }

    @PostMapping("/devices/{product_name}")
    public DeviceDO register(@PathVariable("product_name") String productName){
        return deviceService.addDevice(productName);
    }

    @PostMapping("/devices/emqx_web_hook")
    public Map webHook(@RequestBody Map webHookData){
        deviceService.webHookHandler(webHookData);
       return null;
    }
}
