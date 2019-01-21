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

import com.rackspace.salus.common.messaging.KafkaMessageKey;
import java.util.Map;
// Using the old validation exceptions for podam support
// Will move to the newer ones once they're supported.
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;
import lombok.Data;

@KafkaMessageKey(properties = {"tenantId", "resourceId"})
@Data
public class AttachEvent {
    @NotBlank
    String resourceId;

    @NotEmpty
    Map<String,String> labels;

    @NotBlank
    String envoyId;

    @NotBlank
    String tenantId;

    /**
     * The remote hostname or IP address of the Envoy's TCP connection to the Ambassador.
     * Note that this may be a misleading address if any NAT or proxy is used along the path of
     * the connection.
     */
    @NotNull
    String envoyAddress;
}
