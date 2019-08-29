package com.itheimaxia.easyiothub.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/auth")
    public String checkUser(String clientid,String username,String password){

        return "1";
    }

    @PostMapping("/superuser")
    public String checkSuperUser(String clientid,String username){

        return "1";
    }

    @GetMapping("/acl")
    public String checkAcl(String access,String username,String clientid,String ipaddr,String topic){

        return "1";
    }
}
