package com.itheimaxia.aliyuniotdemo;

import com.aliyun.alink.apiclient.utils.StringUtils;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.Gson;
import com.itheimaxia.aliyuniotdemo.demo.DeviceInfoData;
import com.itheimaxia.aliyuniotdemo.demo.FileUtils;
import com.itheimaxia.aliyuniotdemo.demo.HelloWorld;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AliyunDemoTest {

    private final String productKey = "a1KQ3aGkXG4";
    private final String deviceName = "easy_iot_device1";
    private final String deviceSecret = "mMFME4supxUQy2AlfJYhbp5VwwkXLSs1";
    private final String TAG = "easy_iot_tag";

    @Test
    public void link() throws InterruptedException {
        LinkKitInitParams params = new LinkKitInitParams();
/**
 * 设置 Mqtt 初始化参数
 */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = productKey;
        config.deviceName = deviceName;
        config.deviceSecret = deviceSecret;
/**
 * 是否接受离线消息
 * 对应 mqtt 的 cleanSession 字段
 */
        config.receiveOfflineMsg = false;
        params.mqttClientConfig = config;
/**
 * 设置初始化三元组信息，用户传入
 */
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = productKey;
        deviceInfo.deviceName = deviceName;
        deviceInfo.deviceSecret = deviceSecret;
        params.deviceInfo = deviceInfo;
/**
 * 设置设备当前的初始状态值，属性需要和云端创建的物模型属性一致
 * 如果这里什么属性都不填，物模型就没有当前设备相关属性的初始值。
 * 用户调用物模型上报接口之后，物模型会有相关数据缓存。
 */
        Map<String, ValueWrapper> propertyValues = new HashMap<String, ValueWrapper>();
        params.propertyValues = propertyValues;
// 示例
        propertyValues.put("PowerSwitch", new ValueWrapper.BooleanValueWrapper(1));
        params.propertyValues = propertyValues;
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            public void onError(AError aError) {
                ALog.e(TAG, "Init Error error=" + aError);
            }

            public void onInitDone(InitResult initResult) {
                ALog.i(TAG, "onInitDone result=" + initResult);
            }
        });

        Thread.sleep(10000);
    }

}
