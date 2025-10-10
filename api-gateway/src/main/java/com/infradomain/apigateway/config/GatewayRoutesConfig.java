package com.infradomain.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
      .route("identity-profiles", r -> r
        .path("/identity-profiles/**")
        .filters(f -> f.stripPrefix(1)
          .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST"))
        .uri("lb://identity-profiles"))
      .build();
  }

}
