package com.wszib.gateway_server.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {

    final Logger LOGGER = LoggerFactory.getLogger(ResponseFilter.class);

    private final FilterUtils filterUtils;

    public ResponseFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                LOGGER.debug("Post filter: response code: {}", exchange.getResponse().getStatusCode());
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlationId = filterUtils.getCorrelationId(requestHeaders);
                LOGGER.debug("Adding the correlation id to the outbound headers. {}.", correlationId);
                exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
                LOGGER.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
            }));
        };
    }
}

