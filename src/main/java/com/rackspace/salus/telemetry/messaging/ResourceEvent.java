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

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.rackspace.salus.common.messaging.KafkaMessageKey;
import com.rackspace.salus.telemetry.model.Resource;
import lombok.Data;

@Data
public class ResourceEvent {
    @NotBlank
    Resource resource;

    @NotNull
    Map<String,String> oldLabels;

    @NotBlank
    OperationType operation;

    @NotNull
    boolean presenceMonitorChange;
}