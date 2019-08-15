package com.itheimaxia.easyiot.server.bean;

import com.itheimaxia.easyiot.common.bean.Device;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="devices")
public class DeviceDO extends Device {

}
