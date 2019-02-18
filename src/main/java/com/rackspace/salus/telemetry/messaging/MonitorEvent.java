package com.rackspace.salus.telemetry.messaging;

import java.util.Map;
import lombok.Data;

@Data
public class MonitorEvent {

    String ambassadorId;
    String envoyId;

    //checkData
    Map<String, String> labels;

    //tenantOfEnvoy
    String tenantId;

    String operationType;

    //Optional: for remote checks
    String customerTenantTag;
}
