package com.itheimaxia.easyiotgateway.core.mapping;

import com.itheimaxia.easyiotgateway.config.GatewayProperties;
import com.itheimaxia.easyiotgateway.config.MappingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public abstract class MappingProvider implements IMappingProvider{
    private static final Logger log = LoggerFactory.getLogger(MappingProvider.class);

    protected final GatewayProperties gatewayProperties;

    public MappingProvider(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    protected List<MappingProperties> mappings;

    protected abstract boolean shouldUpdateMappings(HttpServletRequest request);

    protected abstract List<MappingProperties> retrieveMappings();

    public MappingProperties resolveMapping(String originHost, HttpServletRequest request){
        if (shouldUpdateMappings(request)) {
            updateMappings();
        }
        List<MappingProperties> resolvedMappings = mappings.stream()
                .filter(mapping -> originHost.toLowerCase().equals(mapping.getHost().toLowerCase()))
                .collect(Collectors.toList());
        if (isEmpty(resolvedMappings)) {
            return null;
        }
        return resolvedMappings.get(0);
    }

    @PostConstruct
    protected synchronized void updateMappings() {
        List<MappingProperties> newMappings = retrieveMappings();
        //mappingsValidator.validate(newMappings);
        mappings = newMappings;
        //httpClientProvider.updateHttpClients(mappings);
        log.info("Destination mappings updated", mappings);
    }
}
