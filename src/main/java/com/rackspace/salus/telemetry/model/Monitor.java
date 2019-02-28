/*
 *    Copyright 2019 Rackspace US, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *
 */

package com.rackspace.salus.telemetry.model;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "monitors")
@Data
public class Monitor implements Serializable {
    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type="uuid-char")
    UUID id;

    @Column(name="monitor_name")
    String monitorName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="monitor_labels", joinColumns = @JoinColumn(name="id"))
    Map<String,String> labels;

    @NotBlank
    @Column(name="tenant_id")
    String tenantId;

    @NotBlank
    String content;

    @NotNull
    @Column(name="agent_type")
    AgentType agentType;

    @Column(name="target_tenant")
    String targetTenant;

    @Column(name="selector_scope")
    ConfigSelectorScope selectorScope;
}
