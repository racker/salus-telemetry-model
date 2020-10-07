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

package com.rackspace.salus.telemetry.entities;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "agent_history",
    indexes = {
        @Index(name = "by_tenant_envoy_id", columnList = "tenant_id, envoy_id"),
        @Index(name = "by_tenant_resource_id", columnList = "tenant_id, resource_id")
    }
)
public class AgentHistory {

  @Id
  @GeneratedValue
  @org.hibernate.annotations.Type(type="uuid-char")
  UUID id;

  @NotNull
  @Column(name ="connected_timestamp", nullable = false)
  Instant connectedAt;

  @Column(name ="disconnected_timestamp")
  Instant disconnectedAt;

  @NotNull
  @Column(name ="tenant_id", nullable = false)
  String tenantId;

  @NotNull
  @Column(name ="resource_id",nullable = false)
  String resourceId;

  @NotNull
  @Column(name ="envoy_id",nullable = false)
  String envoyId;

  @NotNull
  @Column(name ="remote_ip",nullable = false)
  String remoteIp;

  @Column(name ="zone_id")
  String zoneId;
}
