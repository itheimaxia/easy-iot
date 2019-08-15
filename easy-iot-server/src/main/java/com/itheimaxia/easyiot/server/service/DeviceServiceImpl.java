package com.itheimaxia.easyiot.server.service;

import com.itheimaxia.easyiot.common.bean.Device;
import com.itheimaxia.easyiot.server.bean.DeviceDO;
import com.itheimaxia.easyiot.server.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public DeviceDO addDevice(String productName){
        DeviceDO device = new DeviceDO();
        device.setProduct_name(productName);
        device.setDevice_name(StringUtil.createDeviceName());
        device.setSecret(StringUtil.createDeviceName());
        device.setBroker_username(device.getProduct_name()+"/"+device.getDevice_name());

        mongoTemplate.save(device);

        return device;
    }

}
