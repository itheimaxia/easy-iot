package com.itheimaxia.easyiotgateway.core.interceptor;

import com.itheimaxia.easyiotgateway.config.MappingProperties;
import com.itheimaxia.easyiotgateway.core.http.RequestData;

public class NoOpPreForwardRequestInterceptor implements PreForwardRequestInterceptor {
    @Override
    public void intercept(RequestData data, MappingProperties mapping) {

    }
}
