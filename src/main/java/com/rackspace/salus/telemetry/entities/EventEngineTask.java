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

package com.rackspace.salus.telemetry.entities;

import com.rackspace.salus.telemetry.model.MonitoringSystem;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "event_engine_tasks")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventEngineTask {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue
  @Type(type="uuid-char")
  UUID id;

  @NotNull
  @Column(nullable = false)
  String tenantId;

  @Column
  String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "monitoring_system", nullable = false)
  MonitoringSystem monitoringSystem;

  @NotNull
  @Type(type = "json")
  @Column(nullable = false)
  EventEngineTaskParameters taskParameters;

  @Column(name = "partition_number", nullable = false)
  Integer partition;

  @CreationTimestamp
  @Column(name="created_timestamp")
  Instant createdTimestamp;

  @UpdateTimestamp
  @Column(name="updated_timestamp")
  Instant updatedTimestamp;
}
