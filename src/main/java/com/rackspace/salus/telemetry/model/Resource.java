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

import javax.persistence.*;
// Using the old validation exceptions for podam support
// Will move to the newer ones once they're supported.
//import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "resources",
        uniqueConstraints={@UniqueConstraint(columnNames={"tenant_id","resource_id"})})
@Data
public class Resource implements Serializable {
    @Id
    @GeneratedValue
    Long id;

    @NotNull
    @Column(name="resource_id")
    String resourceId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="resource_labels", joinColumns = @JoinColumn(name="id"))
    Map<String,String> labels;

    @NotBlank
    @Column(name="tenant_id")
    String tenantId;

    @NotNull
    Boolean presenceMonitoringEnabled;

    String region;


}