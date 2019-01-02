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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "resources",
        uniqueConstraints={@UniqueConstraint(columnNames={"tenant_id","identifier_name","identifier_value"})})
@Data
public class Resource {
    @Id
    @GeneratedValue
    Long id;

    @Valid
    ResourceIdentifier resourceIdentifier;

    @ElementCollection
    @CollectionTable(name="labels", joinColumns = @JoinColumn(name="id"))
    @NotNull
    Map<String,String> labels;

    @NotBlank
    @Column(name="tenant_id")
    String tenantId;
}