package com.itheimaxia.easyiot.sdk;

import com.itheimaxia.easyiot.common.bean.Device;
import com.itheimaxia.easyiot.common.util.DeviceUtil;
import com.itheimaxia.easyiot.sdk.mqtt.PushCallback;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Data
public class IotDevice extends Device {

    private String host;
    private String clientId;
    private int timeout = 30;
    private int keepalive = 60;
    private PushCallback pushCallback = new PushCallback();
    private MqttClient client = null;

    private void setEnvProperties(){
        if(StringUtils.isEmpty(host)){
            host = System.getenv().get("EASY_IOT_HOST");
        }
        if(StringUtils.isEmpty(clientId)){
            clientId = System.getenv().get("EASY_IOT_CLIENTID");
        }
        if(StringUtils.isEmpty(getProduct_name())){
            setProduct_name(System.getenv().get("EASY_IOT_PRODUCTNAME"));
        }
        if(StringUtils.isEmpty(getDevice_name())){
            setDevice_name(System.getenv().get("EASY_IOT_DEVICENAME"));
        }
        if(StringUtils.isEmpty(getSecret())){
            setSecret(System.getenv().get("EASY_IOT_SECRET"));
        }
    }

    public void connect(){
        try {
            setEnvProperties();
            client = new MqttClient(host, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(DeviceUtil.getDeviceName(this));
            options.setPassword(getSecret().toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepalive);
            try {
                client.setCallback(pushCallback);
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布，默认qos为0，非持久化
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic,String pushMessage) throws Exception {
        publish(0, false, topic, pushMessage);
    }

    /**
     * 发布
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos,boolean retained,String topic,String pushMessage) throws Exception {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = client.getTopic(topic);
        if(null == mTopic){
            throw new Exception("异常");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题，qos默认为0
     * @param topic
     */
    public void subscribe(String topic){
        subscribe(topic,0);
    }

    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     */
    public void subscribe(String topic,int qos){
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
