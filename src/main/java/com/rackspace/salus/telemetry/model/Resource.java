/*
 *    Copyright 2018 Rackspace US, Inc.
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

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.io.Serializable;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
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
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Data
public class Resource implements Serializable {
    @Id
    @GeneratedValue
    Long id;

    /**
     * This will typically be something like CORE device ID or Xen ID for public cloud VMs.
     * This must be unique for a given tenant.
     */
    @NotNull
    @Column(name="resource_id")
    String resourceId;

    /**
     * Labels are an indexed and query'able aspect of resources that are used for monitor
     * matching.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="resource_labels", joinColumns = @JoinColumn(name="id"))
    Map<String,String> labels;

    /**
     * Unlike labels, metadata is not indexed and not used for resource/monitor matching.
     */
    @Type(type = "json")
    @Column(columnDefinition = "text")
    Map<String,String> metadata;

    @NotBlank
    @Column(name="tenant_id")
    String tenantId;

    @NotNull
    Boolean presenceMonitoringEnabled;

    String region;
}