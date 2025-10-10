package com.infradomain.apigateway.config;

import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * api-gateway
 *
 * @author Juliane Maran
 * @since 08/10/2025
 */
@Configuration
public class CacheConfig {

  @Bean
  public CaffeineCacheManager cacheManager() {
    return new CaffeineCacheManager();
  }

}
