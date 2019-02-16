package com.rackspace.salus.telemetry.messaging;

import java.util.Map;
import lombok.Data;

@Data
public class MonitorEvent {

    public boolean presenceMonitoringEnabled;

    String tenantId;

    String resourceId;

    public Map<OperationType, Map<String, String>> operationOrganizedLabels;
}
