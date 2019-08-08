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

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NamedQueries({
    // NOTE this cannot be a repository method since the query needs to select a resulting
    // type is not an immediate projected field of the repository's entity type
    @NamedQuery(name = "findBoundAgentTypesByResource",
      query = "select distinct b.agentInstall.agentRelease.type from BoundAgentInstall b"
      + " where b.agentInstall.tenantId = :tenantId"
      + "  and b.resourceId = :resourceId")
})
@Table(name = "agent_installs",
  indexes = {
    @Index(name = "by_tenant", columnList = "tenantId")
  }
)
@Data
public class AgentInstall {

  private static final String LABEL_DELIM = "=";

  @Id
  @GeneratedValue
  @org.hibernate.annotations.Type(type="uuid-char")
  UUID id;

  @Column(nullable = false)
  String tenantId;

  @ManyToOne(optional = false)
  AgentRelease agentRelease;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name="agent_install_label_selectors", joinColumns = @JoinColumn(name="agent_install_id"))
  Map<String,String> labelSelector;

  @CreationTimestamp
  @Column(name="created_timestamp")
  Instant createdTimestamp;

  @UpdateTimestamp
  @Column(name="updated_timestamp")
  Instant updatedTimestamp;
}
