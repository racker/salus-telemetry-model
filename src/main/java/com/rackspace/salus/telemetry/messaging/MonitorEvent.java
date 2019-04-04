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

package com.rackspace.salus.telemetry.messaging;

import com.rackspace.salus.telemetry.model.AgentConfig;
import com.rackspace.salus.telemetry.model.Monitor;
import lombok.Data;

@Data
public class MonitorEvent {

    String monitorId;
    String ambassadorId;
    String envoyId;

    //tenantOfEnvoy
    String tenantId;

    //Optional: for remote checks
    String targetTenant;


    public MonitorEvent setFromMonitor(Monitor monitor) {
        tenantId = monitor.getTenantId();
        monitorId = monitor.getId().toString();
        if(monitor.getTargetTenant() != null) {
            targetTenant = monitor.getTargetTenant();
        }
        return this;
    }
}
