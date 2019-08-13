package com.itheimaxia.easyiot.server;

import com.itheimaxia.easyiot.server.mqtt.MqttPushClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EasyIotServerApplication.class})
public class EasyIotServerApplicationTests {

    @Autowired
    public MqttPushClient mqttPushClient;
    @Test
    public void contextLoads() {
        mqttPushClient.subscribe("test");
        mqttPushClient.publish("test","aaaaa");
        System.out.println("发送信息");
    }

}
