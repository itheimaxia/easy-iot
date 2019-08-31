package com.itheimaxia.easyiot.server;

import com.itheimaxia.easyiot.common.config.EasyIotRestConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(value = {EasyIotRestConfig.class})
@SpringBootApplication
@MapperScan("com.itheimaxia.easyiot.server.mapper")
public class EasyIotServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyIotServerApplication.class, args);
    }

}
