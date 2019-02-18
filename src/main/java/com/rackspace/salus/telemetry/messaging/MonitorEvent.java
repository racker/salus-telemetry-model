package com.rackspace.salus.telemetry.messaging;

import java.util.Map;

import com.rackspace.salus.telemetry.model.AgentConfig;
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
}
