package com.itheimaxia.easyiotgateway.core.mapping;

import com.itheimaxia.easyiotgateway.config.MappingProperties;

import javax.servlet.http.HttpServletRequest;

public interface IMappingProvider {
    public MappingProperties resolveMapping(String originHost, HttpServletRequest request) ;
}
