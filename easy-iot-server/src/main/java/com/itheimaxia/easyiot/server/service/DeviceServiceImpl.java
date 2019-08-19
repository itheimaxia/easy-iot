package com.itheimaxia.easyiot.server.service;

import com.itheimaxia.easyiot.common.bean.Device;
import com.itheimaxia.easyiot.common.util.DeviceUtil;
import com.itheimaxia.easyiot.server.bean.ConnectionDO;
import com.itheimaxia.easyiot.server.bean.DeviceDO;
import com.itheimaxia.easyiot.server.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public DeviceDO getDevice(String productName, String deviceName) {
        DeviceDO device =
                mongoTemplate.findOne(Query.query(Criteria.where("product_name").is(productName).
                                and("device_name").is(deviceName)),
                        DeviceDO.class);

        ConnectionDO connectionDO =
                mongoTemplate.findOne(Query.query(Criteria.where("username").is(DeviceUtil.getDeviceName(device))),
                        ConnectionDO.class);
        device.setConnection(connectionDO);
        return device;
    }

    public DeviceDO addDevice(String productName){
        DeviceDO device = new DeviceDO();
        device.setProduct_name(productName);
        device.setDevice_name(StringUtil.createDeviceName());
        device.setSecret(StringUtil.createDeviceName());
        device.setBroker_username(device.getProduct_name()+"/"+device.getDevice_name());

        mongoTemplate.save(device);

        return device;
    }

    private void connected(Map webHook){
        String username = (String) webHook.get("username");
        String[] split = username.split("/");

        DeviceDO device =
                mongoTemplate.findOne(Query.query(Criteria.where("product_name").is(split[0]).
                                and("device_name").is(split[1])),
                        DeviceDO.class);
        if(device == null){
            return;
        }

        ConnectionDO connectionDO = new ConnectionDO();
        connectionDO.setClientId((String)webHook.get("client_id"));
        connectionDO.setConnected(true);
        connectionDO.setConnected_at((Integer)webHook.get("connected_at"));
        connectionDO.setIp_address((String)webHook.get("ipaddress"));
        connectionDO.setKeepalive((Integer) webHook.get("keepalive"));
        connectionDO.setProto_ver((Integer) webHook.get("proto_ver"));
        connectionDO.setUsername(username);
        mongoTemplate.save(connectionDO);
    }

    private void disconnected(Map webHook){
        String username = (String) webHook.get("username");
        Query query = new Query(Criteria.where("_id").is(username));
        Update update = new Update().set("connected", false)
                .set("disconnect_at", new Date().getTime());
        mongoTemplate.updateFirst(query,update,ConnectionDO.class);
    }

    @Override
    public void webHookHandler(Map webHook) {
        switch ((String)webHook.get("action")){
            case "client_connected":
                connected(webHook);
                break;
            case "client_disconnected":
                disconnected(webHook);
                break;
            default:
                break;
        }
    }

}
