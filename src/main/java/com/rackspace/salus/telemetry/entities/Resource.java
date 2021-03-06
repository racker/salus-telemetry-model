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

import com.rackspace.salus.telemetry.model.ValidLabelKeys;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

// Using the old validation exceptions for podam support
// Will move to the newer ones once they're supported.
//import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "resources",
    uniqueConstraints = {
        // also enables indexed query by tenant ID
        @UniqueConstraint(columnNames = {"tenant_id", "resource_id"})
    })
@NamedQueries({
    @NamedQuery(name = "Resource.getDistinctLabels",
    query = "select distinct entry(r.labels) from Resource r where r.tenantId = :tenantId"),
    @NamedQuery(name = "Resource.getMetadata",
    query = "select r.metadata from Resource r where r.tenantId = :tenantId")
})
@Data
public class Resource implements Serializable {
    public static final String REGION_METADATA = "region";

    @Id
    @GeneratedValue
    Long id;

    /**
     * This will typically be something like CORE device ID or Xen ID for public cloud VMs.
     * This must be unique for a given tenant.
     */
    @Pattern(regexp="[A-Za-z0-9.:-]+")
    @NotBlank
    @Column(name="resource_id")
    String resourceId;

    /**
     * Labels are an indexed and query'able aspect of resources that are used for monitor
     * matching.
     */
    @ValidLabelKeys
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name="resource_labels",
        joinColumns = @JoinColumn(name="id"),
        indexes = {
            @Index(columnList = "id,labels_key,labels")
        }
    )
    Map<String,String> labels;

    /**
     * Unlike labels, metadata is not indexed and not used for resource/monitor matching.
     */
    @Type(type = "json")
    @NotNull
    Map<String,String> metadata = new HashMap<>();

    @NotBlank
    @Column(name="tenant_id")
    String tenantId;

    @NotNull
    Boolean presenceMonitoringEnabled;

    /**
     * Indicates if this resource is or ever was associated with an Envoy.
     */
    boolean associatedWithEnvoy;

    @CreationTimestamp
    @Column(name="created_timestamp")
    Instant createdTimestamp;

    @UpdateTimestamp
    @Column(name="updated_timestamp")
    Instant updatedTimestamp;
}