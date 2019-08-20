package com.itheimaxia.easyiot.server.controller;

import com.itheimaxia.easyiot.common.bean.Device;
import com.itheimaxia.easyiot.server.bean.DeviceDO;
import com.itheimaxia.easyiot.server.entity.PageResult;
import com.itheimaxia.easyiot.server.entity.QueryPageBean;
import com.itheimaxia.easyiot.server.service.DeviceService;
import com.itheimaxia.easyiot.server.service.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/{product_name}/{device_name}")
    public DeviceDO register(@PathVariable("product_name") String productName,
                             @PathVariable("device_name") String deviceName){
        return deviceService.getDevice(productName,deviceName);
    }

    @PostMapping("/{product_name}")
    public DeviceDO findDevices(@PathVariable("product_name") String productName){
        return deviceService.addDevice(productName);
    }

    @PostMapping("/pages")
    public PageResult findPages(@RequestBody QueryPageBean page){
        return deviceService.findPages(page);
    }

    @PostMapping("/emqx_web_hook")
    public Map webHook(@RequestBody Map webHookData){
        deviceService.webHookHandler(webHookData);
       return null;
    }
}
