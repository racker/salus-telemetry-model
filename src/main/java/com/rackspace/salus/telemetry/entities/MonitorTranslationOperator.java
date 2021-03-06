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

import com.rackspace.salus.telemetry.model.AgentType;
import com.rackspace.salus.telemetry.model.ConfigSelectorScope;
import com.rackspace.salus.telemetry.model.MonitorType;
import com.rackspace.salus.telemetry.translators.MonitorTranslator;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

/**
 * This entity persists a specific instance of a monitor translation for a given agent type and
 * optionally agent versions, monitor type, and/or selector scope.
 */
@Entity
@Table(name = "monitor_translation_operators", indexes = {
    @Index(name = "monitor_translation_operators_by_agent_type", columnList = "agent_type")
})
@Data
public class MonitorTranslationOperator {

  @Id
  @GeneratedValue
  @org.hibernate.annotations.Type(type="uuid-char")
  UUID id;

  @Column(nullable = false, unique = true)
  String name;

  @Column(name = "description")
  String description;

  @Column(name = "agent_type", nullable = false)
  @Enumerated(EnumType.STRING)
  AgentType agentType;

  /**
   * Optional field that conveys applicable version ranges
   * <a href="https://maven.apache.org/enforcer/enforcer-rules/versionRanges.html">in the form of Maven's version range spec</a>
   */
  @Column(name = "agent_versions")
  String agentVersions;

  /**
   * Optional field that narrows applicability to a specific monitor type.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "monitor_type", nullable = false)
  MonitorType monitorType;

  /**
   * Optional field that narrows applicability to a specific selector scope.
   */
  @Column(name="selector_scope")
  ConfigSelectorScope selectorScope;

  /**
   * Persisted column contains the JSON serialization of a concrete subclass of {@link MonitorTranslator}
   */
  @Column(name = "translator_spec", nullable = false, length = 500)
  @Type(type = "json")
  MonitorTranslator translatorSpec;

  /**
   * Optional field
   * If set, it will be used to determine the order the translations on a monitor are performed in.
   * A translation with an order of 1 will occur before one with an order of 2.
   * If the field is not set it will be treated as 0.
   */
  @Column(name = "priority_order")
  int order;
}
