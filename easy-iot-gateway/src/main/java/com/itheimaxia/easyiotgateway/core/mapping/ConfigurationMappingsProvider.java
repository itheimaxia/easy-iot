package com.itheimaxia.easyiotgateway.core.mapping;

import com.itheimaxia.easyiotgateway.config.GatewayProperties;
import com.itheimaxia.easyiotgateway.config.MappingProperties;
import com.itheimaxia.easyiotgateway.core.http.HttpClientProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationMappingsProvider extends MappingProvider {

    public ConfigurationMappingsProvider(
            GatewayProperties gatewayProperties,
            HttpClientProvider httpClientProvider
    ) {
        super(gatewayProperties,httpClientProvider);
    }


    @Override
    protected boolean shouldUpdateMappings(HttpServletRequest request) {
        return false;
    }

    @Override
    protected List<MappingProperties> retrieveMappings() {
        return gatewayProperties.getMappings().stream()
                .map(MappingProperties::copy)
                .collect(Collectors.toList());
    }
}
