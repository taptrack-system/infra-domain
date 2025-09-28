package com.infradomain.configservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * config-service
 *
 * @author Juliane Maran
 * @since 27/09/2025
 */
@Service
public class PropertyService {

  private static final Logger log = LoggerFactory.getLogger(PropertyService.class);

  @Cacheable(cacheNames = "propertiesCache", key = "#name")
  public String getProperty(String name) {
    log.info("Fetching property '{}' from source (not cache)", name);
    simulateSlowCall();
    return "property-value-" + name;
  }

  private void simulateSlowCall() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Thread interrupted while simulating slow call", e);
    }
  }

}
