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

package com.rackspace.salus.telemetry.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A copy of the core values com.rackspace.monplat.protocol.UniversalMetricFrame.MonitoringSystem
 *
 * This helps reduce the amount of services that depend on umb-protocol.
 *
 * It also makes the systems Salus supports clearer and prevents potentially new incompatable
 * monitoring systems from being automatically included.
 */
public enum MonitoringSystem {
  @JsonProperty("maas")
  MAAS,
  @JsonProperty("uim")
  UIM,
  @JsonProperty("salus")
  SALUS,
  @JsonProperty("scom")
  SCOM,
  @JsonProperty("zenoss")
  ZENOSS,
  @JsonProperty("unrecognized")
  UNRECOGNIZED
}
