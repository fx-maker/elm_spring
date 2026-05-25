package com.neusoft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class CorsConfig {

    private static final String ALLOWED_HEADERS = HttpHeaders.CONTENT_TYPE + "," +
            HttpHeaders.ACCEPT + "," +
            "Authorization,X-Requested-With,requestIP,userId,userType";

    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS,HEAD";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ALLOWED_HEADERS);
                response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHODS);
                response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");

                if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod().name())) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }

            return chain.filter(exchange);
        };
    }
}
