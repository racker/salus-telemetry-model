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

package com.rackspace.salus.telemetry.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.util.Map;

@Data
public class AttachEvent {
    @NotBlank
    String identifierName;

    @NotBlank
    String identifierValue;

    @NotEmpty
    Map<String,String> labels;

    @NotBlank
    String envoyId;

    @NotBlank
    String ambassadorId;

    @NotBlank
    String tenantId;

    @NotNull
    InetSocketAddress address;
}
