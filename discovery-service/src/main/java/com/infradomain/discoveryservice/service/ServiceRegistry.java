package com.infradomain.discoveryservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * discovery-service
 *
 * @author Juliane Maran
 * @since 27/09/2025
 */
@Service
public class ServiceRegistry {

  private static final Logger log = LoggerFactory.getLogger(ServiceRegistry.class);

  @Cacheable(cacheNames = "instancesCache", key = "#serviceId")
  public String getServiceInstances(String serviceId) {
    // Buscando instâncias para o serviço '{}' da fonte (não do cache)
    log.info("Fetching instances for service '{}' from source (not cache)", serviceId);
    // Instâncias para
    return "Instances for " + serviceId;
  }

}
