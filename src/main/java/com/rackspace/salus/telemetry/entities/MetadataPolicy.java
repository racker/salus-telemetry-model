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

import com.rackspace.salus.telemetry.model.MetadataValueType;
import com.rackspace.salus.telemetry.model.MonitorType;
import com.rackspace.salus.telemetry.model.TargetClassName;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "metadata_policies")
@Data
@EqualsAndHashCode(callSuper = true)
public class MetadataPolicy extends Policy {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name="target_class_name")
  TargetClassName targetClassName;

  /**
   * All values are stored as strings, so a type must also be stored
   * so the consumers of these policies know what it should be converted to.
   */
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name="value_type")
  MetadataValueType valueType;

  /**
   * The key does not include the prefix of "rackspace.metadata."
   */
  @NotBlank
  @Column(name="metadata_key")
  String key;

  @NotBlank
  @Column(name="metadata_value")
  String value;
}