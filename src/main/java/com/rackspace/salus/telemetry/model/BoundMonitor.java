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

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

// Using the old validation exceptions for podam support
// Will move to the newer ones once they're supported.
//import javax.validation.constraints.NotBlank;

@Entity @IdClass(BoundMonitor.Id.class)
@Table(name = "boundMonitor",
        uniqueConstraints={@UniqueConstraint(columnNames={"monitor_id","tenant_id", "resource_id", "zone"})})
@Data
public class BoundMonitor implements Serializable {
    public class Id {
        UUID monitorId;
        String tenantId;
        String resourceId;
        String zone;
    }
    @javax.persistence.Id
    @NotNull
    @org.hibernate.annotations.Type(type="uuid-char")
    @ManyToOne
    UUID monitorId;

    @javax.persistence.Id
    @NotBlank
    @Column(name="tenant_id")
    String tenantId;

    @javax.persistence.Id
    @NotNull
    @Column(name="resource_id")
    String resourceId;

    @Column(name="resolved_template")
    String resolvedTemplate;

    @javax.persistence.Id
    @org.hibernate.annotations.Index(name="zone")
    String zone;

    @org.hibernate.annotations.Index(name="envoy_id")
    @Column(name="envoy_id")
    String envoyId;

}