package com.itheimaxia.easyiot.server.bean;

import com.itheimaxia.easyiot.common.bean.Device;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="devices")
@Data
public class DeviceDO extends Device {
    @Id
    String _id;
    ConnectionDO connection;
}
