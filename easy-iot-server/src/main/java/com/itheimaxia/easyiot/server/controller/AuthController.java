package com.itheimaxia.easyiot.server.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/auth")
    public String checkUser(@RequestBody Map param){

        return "1";
    }

    @PostMapping("/superuser")
    public String checkSuperUser(@RequestBody  Map param){

        return "1";
    }

    @GetMapping("/acl")
    public String checkAcl(Map param){

        return "1";
    }
}
