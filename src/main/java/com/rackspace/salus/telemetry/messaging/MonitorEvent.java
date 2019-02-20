package com.rackspace.salus.telemetry.messaging;

import java.util.Map;

import com.rackspace.salus.telemetry.model.AgentConfig;
import com.rackspace.salus.telemetry.model.Monitor;
import lombok.Data;

@Data
public class MonitorEvent {

    String ambassadorId;
    String envoyId;

    //tenantOfEnvoy
    String tenantId;

    OperationType operationType;

    //Optional: for remote checks
    String targetTenant;

    AgentConfig config;


    public MonitorEvent setFromMonitor(Monitor monitor) {
        tenantId = monitor.getTenantId();
        targetTenant = monitor.getTargetTenant();
        config.setContent(monitor.getContent());
        config.setLabels(monitor.getLabels());
        return this;
    }
}
