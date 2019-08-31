package com.itheimaxia.easyiot.server.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection="connection")
public class ConnectionDO implements Serializable {

    private Boolean connected;
    private String clientId;
    private Integer keepalive;
    private String ip_address;
    private Integer proto_ver;
    private Integer connected_at;
    private Integer disconnect_at;
    @Id
    private String username;

}
