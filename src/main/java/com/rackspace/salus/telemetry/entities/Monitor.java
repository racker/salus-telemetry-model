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
import com.rackspace.salus.telemetry.model.LabelSelectorMethod;
import com.rackspace.salus.telemetry.model.MonitorType;
import com.rackspace.salus.telemetry.model.NonMetadataField;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "monitors", indexes = {
    @Index(name = "monitors_by_tenant_and_resource", columnList = "tenant_id, resource_id")
})
@NamedQueries({
    @NamedQuery(name = "Monitor.getDistinctLabelSelectors",
        query = "select distinct entry(m.labelSelector) from Monitor m where m.tenantId = :tenantId"),
    @NamedQuery(name = "Monitor.getTenantsUsingPolicyMetadataInMonitor",
        query = "select distinct m.tenantId from Monitor m join m.monitorMetadataFields "
            + "where :metadataKey member of m.monitorMetadataFields"),
    @NamedQuery(name = "Monitor.getTenantsUsingPolicyMetadataInPlugin",
        query = "select distinct m.tenantId from Monitor m join m.pluginMetadataFields "
            + "where :metadataKey member of m.pluginMetadataFields"),
    @NamedQuery(name = "Monitor.getTenantsUsingZoneMetadata",
        query = "select distinct m.tenantId from Monitor m left outer join m.zones as z where "
            + "m.selectorScope = 'REMOTE' and z.size = 0")
})
@Data
public class Monitor implements Serializable {

    public static final String POLICY_TENANT = "_POLICY_";

    @Id
    @GeneratedValue
    @NonMetadataField
    @org.hibernate.annotations.Type(type="uuid-char")
    UUID id;

    @Column(name="monitor_name")
    String monitorName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="monitor_label_selectors", joinColumns = @JoinColumn(name="monitor_id"))
    @NonMetadataField
    Map<String,String> labelSelector;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="label_selector_method")
    @NonMetadataField
    LabelSelectorMethod labelSelectorMethod = LabelSelectorMethod.AND;

    @NotBlank
    @Column(name="tenant_id")
    @NonMetadataField
    String tenantId;

    @NotBlank
    @Column(length=1000)
    @NonMetadataField
    String content;

    @NotNull
    @Column(name="monitor_type")
    @Enumerated(EnumType.STRING)
    @NonMetadataField
    MonitorType monitorType;

    @NotNull
    @Column(name="agent_type")
    @Enumerated(EnumType.STRING)
    @NonMetadataField
    AgentType agentType;

    @Column(name="selector_scope")
    @Enumerated(EnumType.STRING)
    @NonMetadataField
    ConfigSelectorScope selectorScope;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="monitor_zones", joinColumns = @JoinColumn(name="monitor_id"))
    @NonMetadataField // technically metadata can be used but not in the same way as others
    List<String> zones = new ArrayList<>();

    @Column(name="resource_id")
    @NonMetadataField
    String resourceId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "monitor_excluded_resource_ids", joinColumns = @JoinColumn(name="monitor_id"))
    @NonMetadataField
    Set<String> excludedResourceIds;

    /**
     * General purpose metadata fields.
     */
    @NonMetadataField
    @Type(type = "json")
    Map<String,String> metadata;

    /**
     * Policy management field tracking
     */
    @ElementCollection
    @CollectionTable(name="monitor_metadata_fields", joinColumns = @JoinColumn(name="monitor_id"),
        indexes = @Index(name = "monitors_by_metadata_field", columnList = "monitor_id"))
    @NonMetadataField
    List<String> monitorMetadataFields;

    @ElementCollection
    @CollectionTable(name="plugin_metadata_fields", joinColumns = @JoinColumn(name="monitor_id"),
        indexes = @Index(name = "monitors_by_plugin_metadata_field", columnList = "monitor_id"))
    @NonMetadataField
    List<String> pluginMetadataFields;

    @NotNull
    @Column(name = "monitoring_interval") // just "interval" conflicts with SQL identifiers
    Duration interval;

    @Column(name = "policy_id")
    @NonMetadataField
    @org.hibernate.annotations.Type(type="uuid-char")
    UUID policyId;

    @CreationTimestamp
    @Column(name="created_timestamp")
    @NonMetadataField
    Instant createdTimestamp;

    @UpdateTimestamp
    @Column(name="updated_timestamp")
    @NonMetadataField
    Instant updatedTimestamp;

    @Override
    public String toString() {
        // This must be overridden otherwise the lazily loaded attributes will lead to exceptions.
        return String.format("id=%s, name=%s, tenantId=%s, monitorType=%s, resourceId=%s, "
                + "excludedResourceIds=%s, metadata=%s, "
                + "selectorScope=%s, labelSelector=%s, labelSelectorMethod=%s, zones=%s, interval=%s",
            id, monitorName, tenantId, monitorType, resourceId,
            excludedResourceIds, metadata,
            selectorScope, labelSelector,
            labelSelectorMethod, zones, interval);
    }
}

