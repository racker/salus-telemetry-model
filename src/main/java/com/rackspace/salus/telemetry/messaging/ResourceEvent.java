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

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@KafkaMessageKey(properties = {"tenantId", "resourceId"})
public class ResourceEvent {
    @NotBlank
    String tenantId;

    @NotBlank
    String resourceId;

    /**
     * Indicates that the labels or metadata of the resource have changed.
     * Label selection should and content rendering should be re-evaluated.
     */
    boolean labelsChanged;

    /**
     * Indicates that the resource was deleted.
     */
    boolean deleted;

    /**
     * When non-null, indicates that a new Envoy instance has re-attached for this resource.
     */
    String reattachedEnvoyId;
}
