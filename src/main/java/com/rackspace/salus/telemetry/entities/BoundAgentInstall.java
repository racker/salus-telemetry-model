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

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@IdClass(BoundAgentInstall.PrimaryKey.class)
@Table(name = "bound_agent_installs")
@Data
public class BoundAgentInstall {

  @Data
  public static class PrimaryKey implements Serializable {

    @org.hibernate.annotations.Type(type = "uuid-char")
    UUID agentInstall;
    String resourceId;
  }

  @Id
  @ManyToOne
  AgentInstall agentInstall;

  @Id
  String resourceId;
}
