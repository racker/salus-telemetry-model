/*
 * Copyright 2020 Rackspace US, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rackspace.salus.telemetry.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RepositoryCacheProperties.class)
@EnableCaching
public class RepositoryCacheConfig {

  private final RepositoryCacheProperties properties;

  @Autowired
  public RepositoryCacheConfig(RepositoryCacheProperties properties) {
    this.properties = properties;
  }

  @Bean
  public JCacheManagerCustomizer repositoryCacheCustomizer() {
    return cacheManager -> {
      cacheManager.createCache("tenant_ids", tenantCacheConfig());
    };
  }

  private javax.cache.configuration.Configuration<Object, Object> tenantCacheConfig() {
    return Eh107Configuration.fromEhcacheCacheConfiguration(
        CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
            ResourcePoolsBuilder.heap(properties.getTenantSize())
        )
            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(properties.getTtl()))
    );
  }
}
