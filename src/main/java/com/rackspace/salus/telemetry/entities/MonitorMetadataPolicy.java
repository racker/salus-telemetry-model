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

import com.rackspace.salus.telemetry.model.MonitorType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "monitor_metadata_policies")
@Data
@EqualsAndHashCode(callSuper = true)
public class MonitorMetadataPolicy extends MetadataPolicy {

  /**
   * If left blank, this will apply to all monitor types.
   * Otherwise the value should match the plugin type provided when a monitor is created.
   * e.g. ping, http_response, x509_cert, etc.
   */
  @Enumerated(EnumType.STRING)
  @Column(name="monitor_type")
  MonitorType monitorType;
}
