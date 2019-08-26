package com.itheimaxia.easyiotgateway.core.interceptor;

import com.itheimaxia.easyiotgateway.config.MappingProperties;
import com.itheimaxia.easyiotgateway.core.http.ResponseData;

public interface PostForwardResponseInterceptor {
    void intercept(ResponseData data, MappingProperties mapping);
}
