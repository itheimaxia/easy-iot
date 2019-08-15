package com.itheimaxia.easyiot.common.bean;

import java.io.Serializable;

public class Device implements Serializable {
    private String product_name;
    private String device_name;
    private String broker_username;
    private String secret;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getBroker_username() {
        return broker_username;
    }

    public void setBroker_username(String broker_username) {
        this.broker_username = broker_username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "Device{" +
                "product_name='" + product_name + '\'' +
                ", device_name='" + device_name + '\'' +
                ", broker_username='" + broker_username + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
