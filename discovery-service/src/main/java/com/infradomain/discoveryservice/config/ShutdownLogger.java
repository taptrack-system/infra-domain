package com.infradomain.discoveryservice.config;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * discovery-service
 *
 * @author Juliane Maran
 * @since 27/09/2025
 */
@Configuration
public class ShutdownLogger {

  private static final Logger log = LoggerFactory.getLogger(ShutdownLogger.class);

  @PreDestroy
  public void onShutdown() {
    log.info("Discovery Service shutting down gracefully... Eureka connections will be closed.");
  }

}
