package com.itheimaxia.easyiotgateway.core.mapping;

import com.itheimaxia.easyiotgateway.config.GatewayProperties;
import com.itheimaxia.easyiotgateway.config.MappingProperties;
import com.itheimaxia.easyiotgateway.core.exception.GatewayException;
import com.itheimaxia.easyiotgateway.core.http.HttpClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.springframework.util.CollectionUtils.isEmpty;

public abstract class MappingProvider implements IMappingProvider{
    private static final Logger log = LoggerFactory.getLogger(MappingProvider.class);

    protected final GatewayProperties gatewayProperties;
    protected final HttpClientProvider httpClientProvider;

    public MappingProvider(GatewayProperties gatewayProperties, HttpClientProvider httpClientProvider) {
        this.gatewayProperties = gatewayProperties;
        this.httpClientProvider = httpClientProvider;
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

    protected void validateDestinations(MappingProperties mapping) {
        if (isEmpty(mapping.getDestinations())) {
            throw new GatewayException("No destination hosts for mapping" + mapping);
        }
        List<String> correctedHosts = new ArrayList<>(mapping.getDestinations().size());
        mapping.getDestinations().forEach(destination -> {
            if (isBlank(destination)) {
                throw new GatewayException("Empty destination for mapping " + mapping);
            }
            if (!destination.matches(".+://.+")) {
                destination = "http://" + destination;
            }
            destination = removeEnd(destination, "/");
            correctedHosts.add(destination);
        });
        mapping.setDestinations(correctedHosts);
    }

    @PostConstruct
    protected synchronized void updateMappings() {
        List<MappingProperties> newMappings = retrieveMappings();
        //mappingsValidator.validate(newMappings);
        mappings = newMappings;
        mappings.stream().forEach(mapping ->{
            validateDestinations(mapping);
        });

        httpClientProvider.updateHttpClients(mappings);
        log.info("Destination mappings updated", mappings);
    }
}
