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

package com.rackspace.salus.telemetry.repositories;

import com.rackspace.salus.telemetry.entities.MonitorMetadataPolicy;
import com.rackspace.salus.telemetry.model.MonitorType;
import com.rackspace.salus.telemetry.model.PolicyScope;
import com.rackspace.salus.telemetry.model.TargetClassName;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MonitorMetadataPolicyRepository extends PagingAndSortingRepository<MonitorMetadataPolicy, UUID> {

  boolean existsByScopeAndSubscopeAndTargetClassNameAndMonitorTypeAndKey(
      PolicyScope policyScope, String subscope, TargetClassName className, MonitorType type, String key);

  Optional<MonitorMetadataPolicy> findByScopeAndTargetClassNameAndKey(PolicyScope policyScope, TargetClassName className, String key);


}
