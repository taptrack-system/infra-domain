package com.infradomain.gatewayservice.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway-service
 *
 * @author Juliane Maran
 * @since 25/09/2025
 */
@Component
public class LoggingFilter implements GlobalFilter {

  private static final Logger log = LogManager.getLogger(LoggingFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("Request path: {}", exchange.getRequest().getPath());
    return chain.filter(exchange);
  }

}
