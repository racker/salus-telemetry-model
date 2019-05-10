/*
 * Copyright 2019 Rackspace US, Inc.
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

package com.rackspace.salus.telemetry.messaging;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Indicates that an Envoy-Resource has reattached to a pre-existing resource entry in a zone.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReattachedResourceZoneEvent extends ZoneEvent {
  String fromEnvoyId;
  String toEnvoyId;
}