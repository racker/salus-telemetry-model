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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

@Data
@ConfigurationProperties("salus.repository.cache")
public class RepositoryCacheProperties {
  /**
   * Maximum cache size of policy references.
   */
  long tenantSize = 10_000;
  /**
   * Duration to expire cache entries after creation.
   */
  @DurationUnit(ChronoUnit.MINUTES)
  Duration ttl = Duration.ofMinutes(5);
}
