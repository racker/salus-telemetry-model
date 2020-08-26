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

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
@Data
public class EventStateChange implements Serializable {

  @Id
  @GeneratedValue
  @org.hibernate.annotations.Type(type="uuid-char")
  UUID id;

  @Column(name="tenant_id")
  String tenantId;

  @Column(name="resource_id")
  String resourceId;

  @Column(name="monitor_id")
  UUID monitorId;

  @Column(name="task_id")
  UUID taskId;

  @Column(name="state")
  String state;

  @Column(name="message")
  String message;

  // a serialized list of KapacitorEvents from salus-event-engine-common
  @Column(name="contributing_events")
  String contributingEvents;

  @Column(name="previous_state")
  String previousState;

  @Column(name="metric_timestamp")
  Instant metricTimestamp;

  @Column(name="evaluation_timestamp")
  Instant evaluationTimestamp;

}
