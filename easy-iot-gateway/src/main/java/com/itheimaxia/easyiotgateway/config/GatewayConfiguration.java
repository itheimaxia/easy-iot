package com.itheimaxia.easyiotgateway.config;

import com.itheimaxia.easyiotgateway.core.http.HttpClientProvider;
import com.itheimaxia.easyiotgateway.core.http.RequestDataExtractor;
import com.itheimaxia.easyiotgateway.core.http.RequestForwarder;
import com.itheimaxia.easyiotgateway.core.http.ReverseProxyFilter;
import com.itheimaxia.easyiotgateway.core.interceptor.NoOpPostForwardResponseInterceptor;
import com.itheimaxia.easyiotgateway.core.interceptor.NoOpPreForwardRequestInterceptor;
import com.itheimaxia.easyiotgateway.core.interceptor.PostForwardResponseInterceptor;
import com.itheimaxia.easyiotgateway.core.interceptor.PreForwardRequestInterceptor;
import com.itheimaxia.easyiotgateway.core.loadbalance.LoadBalancer;
import com.itheimaxia.easyiotgateway.core.loadbalance.RandomLoadBalancer;
import com.itheimaxia.easyiotgateway.core.mapping.ConfigurationMappingsProvider;
import com.itheimaxia.easyiotgateway.core.mapping.MappingProvider;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties({GatewayProperties.class})
public class GatewayConfiguration {
    protected final GatewayProperties faradayProperties;
    protected final ServerProperties serverProperties;

    public GatewayConfiguration(GatewayProperties faradayProperties,
                                ServerProperties serverProperties) {
        this.faradayProperties = faradayProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    public FilterRegistrationBean<ReverseProxyFilter> faradayReverseProxyFilterRegistrationBean(
            ReverseProxyFilter proxyFilter) {
        FilterRegistrationBean<ReverseProxyFilter> registrationBean = new FilterRegistrationBean<>(proxyFilter);
        registrationBean.setOrder(faradayProperties.getFilterOrder()); // by default to Ordered.HIGHEST_PRECEDENCE + 100
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public ReverseProxyFilter faradayReverseProxyFilter(
            RequestDataExtractor extractor,
            MappingProvider mappingsProvider,
            RequestForwarder requestForwarder,
            PreForwardRequestInterceptor requestInterceptor
    ) {
        return new ReverseProxyFilter(faradayProperties, extractor, mappingsProvider,
                requestForwarder, requestInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClientProvider faradayHttpClientProvider() {
        return new HttpClientProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestDataExtractor faradayRequestDataExtractor() {
        return new RequestDataExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingProvider faradayConfigurationMappingsProvider(HttpClientProvider httpClientProvider) {
            return new ConfigurationMappingsProvider(
                    faradayProperties,httpClientProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancer faradayLoadBalancer() {
        return new RandomLoadBalancer();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestForwarder faradayRequestForwarder(
            HttpClientProvider httpClientProvider,
            MappingProvider mappingsProvider,
            LoadBalancer loadBalancer,
            Optional<MeterRegistry> meterRegistry,
            PostForwardResponseInterceptor responseInterceptor
    ) {
        return new RequestForwarder(
                serverProperties, faradayProperties, httpClientProvider,
                mappingsProvider, loadBalancer, meterRegistry, responseInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public PreForwardRequestInterceptor faradayPreForwardRequestInterceptor() {
        return new NoOpPreForwardRequestInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public PostForwardResponseInterceptor faradayPostForwardResponseInterceptor() {
        return new NoOpPostForwardResponseInterceptor();
    }
}
