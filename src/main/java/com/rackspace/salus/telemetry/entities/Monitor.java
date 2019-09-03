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

import com.rackspace.salus.telemetry.model.AgentType;
import com.rackspace.salus.telemetry.model.ConfigSelectorScope;
import com.rackspace.salus.telemetry.model.LabelSelectorMethod;
import com.rackspace.salus.telemetry.model.MonitorType;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "monitors", indexes = {
    @Index(name = "monitors_by_tenant_and_resource", columnList = "tenant_id, resource_id")
})
@NamedQueries({
    @NamedQuery(name = "Monitor.getDistinctLabelSelectors",
        query = "select distinct entry(m.labelSelector) from Monitor m where m.tenantId = :tenantId"),
    @NamedQuery(name = "Monitor.getTenantsUsingTemplateVariable",
        query = "select distinct m.tenantId from Monitor m join m.templateVariables "
            + "where :variable member of m.templateVariables")
})
@Data
public class Monitor implements Serializable {

    public static final String POLICY_TENANT = "_POLICY_";

    @Id
    @GeneratedValue
    @org.hibernate.annotations.Type(type="uuid-char")
    UUID id;

    @Column(name="monitor_name")
    String monitorName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="monitor_label_selectors", joinColumns = @JoinColumn(name="monitor_id"))
    Map<String,String> labelSelector;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="label_selector_method")
    LabelSelectorMethod labelSelectorMethod = LabelSelectorMethod.AND;

    @NotBlank
    @Column(name="tenant_id")
    String tenantId;

    @NotBlank
    String content;

    @NotNull
    @Column(name="monitor_type")
    @Enumerated(EnumType.STRING)
    MonitorType monitorType;

    @NotNull
    @Column(name="agent_type")
    @Enumerated(EnumType.STRING)
    AgentType agentType;

    @Column(name="selector_scope")
    @Enumerated(EnumType.STRING)
    ConfigSelectorScope selectorScope;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="monitor_zones", joinColumns = @JoinColumn(name="monitor_id"))
    List<String> zones;

    @Column(name="resource_id")
    String resourceId;

    @ElementCollection
    @CollectionTable(name="monitor_template_variables", joinColumns = @JoinColumn(name="monitor_id"),
        indexes = @Index(name = "monitors_by_template_variable", columnList = "monitor_id"))
    List<String> templateVariables;

    @Column(name = "monitoring_interval") // just "interval" conflicts with SQL identifiers
    Duration interval;

    @CreationTimestamp
    @Column(name="created_timestamp")
    Instant createdTimestamp;

    @UpdateTimestamp
    @Column(name="updated_timestamp")
    Instant updatedTimestamp;
}
