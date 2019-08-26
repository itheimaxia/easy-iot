package com.itheimaxia.easyiotgateway.core.http;

import com.itheimaxia.easyiotgateway.config.GatewayProperties;
import com.itheimaxia.easyiotgateway.config.MappingProperties;
import com.itheimaxia.easyiotgateway.core.exception.GatewayException;
import com.itheimaxia.easyiotgateway.core.interceptor.PreForwardRequestInterceptor;
import com.itheimaxia.easyiotgateway.core.mapping.MappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

public class ReverseProxyFilter extends OncePerRequestFilter {

    protected static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    protected static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
    protected static final String X_FORWARDED_HOST_HEADER = "X-Forwarded-Host";
    protected static final String X_FORWARDED_PORT_HEADER = "X-Forwarded-Port";

    private static final Logger log = LoggerFactory.getLogger(ReverseProxyFilter.class);

    protected final GatewayProperties faradayProperties;
    protected final RequestDataExtractor extractor;
    protected final MappingProvider mappingsProvider;
    protected final RequestForwarder requestForwarder;
//    protected final ProxyingTraceInterceptor traceInterceptor;
    protected final PreForwardRequestInterceptor preForwardRequestInterceptor;

    public ReverseProxyFilter(
            GatewayProperties faradayProperties,
            RequestDataExtractor extractor,
            MappingProvider mappingsProvider,
            RequestForwarder requestForwarder,
//            ProxyingTraceInterceptor traceInterceptor,
            PreForwardRequestInterceptor requestInterceptor
    ) {
        this.faradayProperties = faradayProperties;
        this.extractor = extractor;
        this.mappingsProvider = mappingsProvider;
        this.requestForwarder = requestForwarder;
//        this.traceInterceptor = traceInterceptor;
        this.preForwardRequestInterceptor = requestInterceptor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String originUri = extractor.extractUri(request);
        String originHost = extractor.extractHost(request);

        log.debug("Incoming request", "method", request.getMethod(),
                "host", originHost,
                "uri", originUri);

        HttpHeaders headers = extractor.extractHttpHeaders(request);
        HttpMethod method = extractor.extractHttpMethod(request);

//        String traceId = traceInterceptor.generateTraceId();
//        traceInterceptor.onRequestReceived(traceId, method, originHost, originUri, headers);

        MappingProperties mapping = mappingsProvider.resolveMapping(originHost, request);
        if (mapping == null) {
//            traceInterceptor.onNoMappingFound(traceId, method, originHost, originUri, headers);

            log.debug(String.format("Forwarding: %s %s %s -> no mapping found", method, originHost, originUri));

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Unsupported domain");
            return;
        } else {
            log.debug(String.format("Forwarding: %s %s %s -> %s", method, originHost, originUri, mapping.getDestinations()));
        }

        byte[] body = extractor.extractBody(request);
        addForwardHeaders(request, headers);

        RequestData dataToForward = new RequestData(method, originHost, originUri, headers, body, request);
        preForwardRequestInterceptor.intercept(dataToForward, mapping);
        if (dataToForward.isNeedRedirect() && !isBlank(dataToForward.getRedirectUrl())) {
            log.debug(String.format("Redirecting to -> %s", dataToForward.getRedirectUrl()));
            response.sendRedirect(dataToForward.getRedirectUrl());
            return;
        }

        ResponseEntity<byte[]> responseEntity =
                requestForwarder.forwardHttpRequest(dataToForward, null, mapping);
        this.processResponse(response, responseEntity);
    }

    protected void addForwardHeaders(HttpServletRequest request, HttpHeaders headers) {
        List<String> forwordedFor = headers.get(X_FORWARDED_FOR_HEADER);
        if (isEmpty(forwordedFor)) {
            forwordedFor = new ArrayList<>(1);
        }
        forwordedFor.add(request.getRemoteAddr());
        headers.put(X_FORWARDED_FOR_HEADER, forwordedFor);
        headers.set(X_FORWARDED_PROTO_HEADER, request.getScheme());
        headers.set(X_FORWARDED_HOST_HEADER, request.getServerName());
        headers.set(X_FORWARDED_PORT_HEADER, valueOf(request.getServerPort()));
    }


    protected void processResponse(HttpServletResponse response, ResponseEntity<byte[]> responseEntity) {
        response.setStatus(responseEntity.getStatusCode().value());
        responseEntity.getHeaders().forEach((name, values) ->
                values.forEach(value -> response.addHeader(name, value))
        );
        if (responseEntity.getBody() != null) {
            try {
                response.getOutputStream().write(responseEntity.getBody());
            } catch (IOException e) {
                throw new GatewayException("Error writing body of HTTP response", e);
            }
        }
    }
}