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

import com.rackspace.salus.telemetry.model.JobType;
import com.rackspace.salus.telemetry.model.JobStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "jobs")
@Data
public class Job implements Serializable {

  @Id
  String id;

  @Column(name = "tenant_id")
  String tenantId;

  @Column
  @NotNull
  @Enumerated(EnumType.STRING)
  JobType type;

  @Column
  @NotNull
  @Enumerated(EnumType.STRING)
  JobStatus status;

  /**
   * This field is used to store the details of job result or exceptions in case of any failure.
   */
  @Column
  @Type(type = "text")
  String description;

  @CreationTimestamp
  @Column(name = "created_timestamp")
  Instant createdTimestamp;

  @UpdateTimestamp
  @Column(name = "updated_timestamp")
  Instant updatedTimestamp;
}
