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

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

// Using the old validation exceptions for podam support
// Will move to the newer ones once they're supported.
//import javax.validation.constraints.NotBlank;

/**
 * This entity tracks the three-way binding of
 * <ul>
 *   <li>monitor</li>
 *   <li>resource</li>
 *   <li>zone (empty string for local/agent monitors)</li>
 * </ul>
 *
 * <p>
 * As part of the binding, the agent configuration content of the monitor is stored in its
 * rendered form with all context placeholders replaced with their per-binding values.
 * </p>
 *
 * <p>
 *   This entity has a corresponding DTO at
 *   {@link com.rackspace.salus.monitor_management.web.model.BoundMonitorDTO}
 *   where the fields of that class must be maintained to align with the fields of this entity.
 * </p>
 */
@Entity
@IdClass(BoundMonitor.PrimaryKey.class)
@Table(name = "bound_monitors",
indexes = {
    @Index(name = "by_envoy_id", columnList = "envoy_id"),
    @Index(name = "by_zone_envoy", columnList = "zone_name,envoy_id"),
    @Index(name = "by_resource", columnList = "resource_id")
})
@NamedQueries({
    /*
     * @param zoneName
     * @return (pollerResourceId, load)
     */
    @NamedQuery(name = "BoundMonitor.publicPollerLoading", query =
        "select b.pollerResourceId as pollerResourceId, count(b) as load"
            + " from BoundMonitor b"
            + " where b.zoneName = :zoneName"
            + "  and b.pollerResourceId is not null"
            + " group by b.pollerResourceId"
            + " order by load asc"),
    /*
     * @param tenantId
     * @param zoneName
     * @return (pollerResourceId, load)
     */
    @NamedQuery(name = "BoundMonitor.privatePollerLoading", query =
        "select b.pollerResourceId as pollerResourceId, count(b) as load"
            + " from BoundMonitor b"
            + " where b.tenantId = :tenantId"
            + "  and b.zoneName = :zoneName"
            + "  and b.pollerResourceId is not null"
            + " group by b.pollerResourceId"
            + " order by load asc")
})
@Data
public class BoundMonitor implements Serializable {

  @Data
  public static class PrimaryKey implements Serializable {

    /**
     * The Java and Hibernate type of <code>monitor</code> needs to match the primary key
     * of {@link Monitor}
     */
    @org.hibernate.annotations.Type(type="uuid-char")
    UUID monitor;
    String tenantId;
    String resourceId;
    String zoneName;
  }

  @Id
  @ManyToOne
  Monitor monitor;

  @NotBlank
  @Column(name="tenant_id")
  String tenantId;

  /**
   * For remote monitors, contains the binding of a specific monitoring zone.
   * For local monitors, this field is an empty string.
   */
  @Id
  @NotNull
  @Column(name="zone_name", length = 100)
  String zoneName;

  /**
   * Contains the binding of the {@link Monitor} (via <code>monitorId</code>) to a specific
   * resource to be monitored.
   */
  @Id
  @NotNull
  @Column(name="resource_id", length = 100)
  String resourceId;

  @Lob
  @Column(name="rendered_content")
  String renderedContent;

  @Column(name="envoy_id", length = 100)
  String envoyId;

  @Column(name="poller_resource_id", length = 100)
  String pollerResourceId;

  @CreationTimestamp
  @Column(name="created_timestamp")
  Instant createdTimestamp;

  @UpdateTimestamp
  @Column(name="updated_timestamp")
  Instant updatedTimestamp;
}