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

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tenant_metadata", indexes = {
    @Index(name = "by_tenant", columnList = "tenant_id"),
    @Index(name = "by_account_type", columnList = "account_type")
})
@NamedQueries({
    @NamedQuery(name = "TenantMetadata.getByAccountType",
        query = "select distinct t.tenantId from TenantMetadata t where t.accountType = :accountType")
})
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Data
public class TenantMetadata {
  @Id
  @GeneratedValue
  @org.hibernate.annotations.Type(type="uuid-char")
  UUID id;

  @NotBlank
  @Column(name = "tenant_id", unique = true)
  String tenantId;

  @Column(name = "account_type")
  String accountType;

  @NotNull
  @Type(type = "json")
  @Column(columnDefinition = "text")
  Map<String, String> metadata;

  @CreationTimestamp
  @Column(name="created_timestamp")
  Instant createdTimestamp;

  @UpdateTimestamp
  @Column(name="updated_timestamp")
  Instant updatedTimestamp;
}