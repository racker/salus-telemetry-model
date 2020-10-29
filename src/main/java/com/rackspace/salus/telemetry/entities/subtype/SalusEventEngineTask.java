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

package com.rackspace.salus.telemetry.entities.subtype;

import com.rackspace.salus.telemetry.entities.EventEngineTask;
import com.rackspace.salus.telemetry.model.ConfigSelectorScope;
import com.rackspace.salus.telemetry.model.MonitorType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "salus_event_engine_tasks")
@Data
@EqualsAndHashCode(callSuper = true)
public class SalusEventEngineTask extends EventEngineTask {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "monitor_type", nullable = false)
  MonitorType monitorType;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "monitor_selector_scope", nullable = false)
  ConfigSelectorScope monitorScope;
}
