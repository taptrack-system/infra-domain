package com.infradomain.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gateway-service
 *
 * @author Juliane Maran
 * @since 25/09/2025
 */
@Configuration
public class GatewayConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
      .route("service-a", r -> r.path("/service-a/**")
        .filters(f -> f.stripPrefix(1)) // Remove o primeiro segmento do caminho antes de encaminhar
        .uri("lb://service-a")) // Usa o Load Balancer do Eureka
      .route("service-b", r -> r.path("/service-b/**")
        .filters(f -> f.stripPrefix(1))
        .uri("lb://service-b"))
      .build();
  }

}
