package com.itheimaxia.easyiot.server.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheimaxia.easyiot.api.bean.Device;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@TableName("device")
public class DeviceDO extends Device {
    @Id
    Long id;
}
