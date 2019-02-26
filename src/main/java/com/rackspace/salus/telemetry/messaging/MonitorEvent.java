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
        if(monitor.getTargetTenant() != null) {
            targetTenant = monitor.getTargetTenant();
        }
        if(config == null) {
            config = new AgentConfig();
        }
        if(monitor.getContent() != null) {
            config.setContent(monitor.getContent());
        }
        if(monitor.getLabels() != null) {
            config.setLabels(monitor.getLabels());
        }
        return this;
    }
}
