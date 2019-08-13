package com.itheimaxia.easyiot.server.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(MqttConfiguration.PREFIX)
@Data
public class MqttConfiguration {

    @Autowired
    private MqttPushClient mqttPushClient;

    public static final String PREFIX = "mqtt";
    private String host;
    private String clientid;
    private String username;
    private String password;
    private String topic;
    private int timeout;
    private int keepalive;


    @Bean
    public MqttPushClient getMqttPushClient(){
        mqttPushClient.connect(host, clientid, username, password, timeout,keepalive);
        return mqttPushClient;
    }
}
