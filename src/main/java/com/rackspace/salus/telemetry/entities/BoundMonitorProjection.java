package com.rackspace.salus.telemetry.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoundMonitorProjection {

  String envoyId;
  Monitor monitor;
  String tenantId;
  String zoneName;
  String resourceId;
}
