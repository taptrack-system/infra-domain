package com.infradomain.gatewayservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * gateway-service
 *
 * @author Juliane Maran
 * @since 27/09/2025
 */
@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  @Cacheable(cacheNames = "usersCache", key = "#id")
  public String getUserById(Long id) {
    log.info("Fetching user with ID {} form source (not cache)", id);
    simulateSlowCall();
    return "User-" + id;
  }

  @CacheEvict(cacheNames = "usersCache", key = "#id")
  public void evictUser(Long id) {
    log.info("Evicting user with ID {} from cache", id);
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
