package com.wszib.gateway_server.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
class TrackingFilter implements GlobalFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingFilter.class);

    private final FilterUtils filterUtils;

    TrackingFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(requestHeaders)){
            LOGGER.debug("org-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId(requestHeaders));
        } else{
           String correlationId = generateCorrelationId();
              exchange = filterUtils.setCorrelationId(exchange, correlationId);
              LOGGER.debug("org-correlation-id generated in tracking filter: {}.", correlationId);
        }
        return chain.filter(exchange);
    }


    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders){
        return filterUtils.getCorrelationId(requestHeaders) != null;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
