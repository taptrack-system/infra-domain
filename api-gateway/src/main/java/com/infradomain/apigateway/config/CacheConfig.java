package com.infradomain.apigateway.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

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
    var manager = new CaffeineCacheManager("routes", "someOtherCache");
    manager.setCaffeine(Caffeine.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(10))
      .maximumSize(1000));
    return manager;
  }

}
