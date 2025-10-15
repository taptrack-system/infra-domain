package com.infradomain.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.infradomain.apigateway.constants.HttpHeadersConstants.*;

/**
 *
 * @author Juliane Maran
 * <p>
 * Configuração das rotas do Spring Cloud Gateway e do CORS.
 */
@Configuration
public class GatewayRoutesConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
      // Rota única para o microsserviço Identity Profiles
      .route("identity-profiles-route", r -> r
        .path("/identity-profiles/**")
        .filters(f -> f
          // Mantém o prefixo, pois o service também usa /identity-profiles no context-path
          .dedupeResponseHeader(ACCESS_CONTROL_ALLOW_ORIGIN, STRATEGY_RETAIN_FIRST)
        )
        .uri("lb://identity-profiles"))
      // Rota única para o microsserviço Billing Service
      .route("billing-service-route", r -> r
        .path("/billing/**")
        .filters(f -> f
          // Mantém o prefixo, pois o service também usa /identity-profiles no context-path
          .dedupeResponseHeader(ACCESS_CONTROL_ALLOW_ORIGIN, STRATEGY_RETAIN_FIRST)
        )
        .uri("lb://billing-service"))
      .build();
  }

  /**
   * Configuração global de CORS para o gateway.
   */
  @Bean
  public CorsWebFilter corsWebFilter() {
    var config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOriginPatterns(List.of("http://localhost:4200"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.addExposedHeader(AUTHORIZATION);
    config.addExposedHeader(CONTENT_TYPE);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsWebFilter(source);
  }

}
