package com.wszib.gateway_server.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

@Component
public class FilterUtils {

    public static final String CORRELATION_ID = "org-correlation-id";
    public static final String AUTH_TOKEN     = "org-auth-token";
    public static final String USER_ID        = "org-user-id";
    public static final String ORG_ID         = "org-org-id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public String getCorrelationId(HttpHeaders requestHeaders){
        return requestHeaders.getFirst(CORRELATION_ID);
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header(name, value)
                                .build())
                .build();
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

}
