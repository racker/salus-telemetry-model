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

import com.rackspace.salus.telemetry.entities.Monitor;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MonitorRepository extends PagingAndSortingRepository<Monitor, UUID> {

    boolean existsByIdAndTenantId(UUID id, String tenantId);

    List<Monitor> findByIdIn(List<UUID> ids);

    Page<Monitor> findByIdIn(List<UUID> ids, Pageable pageable);

    Optional<Monitor> findByIdAndTenantId(UUID id, String tenantId);

    Page<Monitor> findByTenantId(String tenantId, Pageable pageable);

    List<Monitor> findByTenantIdAndResourceId(String tenantId, String resourceId);

    @Query("select m from Monitor m join fetch m.templateVariables where :variable member of m.templateVariables")
    Set<Monitor> findByTemplateVariablesContaining(String variable);

    @Query("select m from Monitor m where m.tenantId = :tenantId and :zone member of m.zones")
    Page<Monitor> findByTenantIdAndZonesContains(String tenantId, String zone, Pageable page);

    @Query("select count(m) from Monitor m where :zone member of m.zones")
    int countAllByZonesContains(String zone);

    @Query("select count(m) from Monitor m where m.tenantId = :tenantId and :zone member of m.zones")
    int countAllByTenantIdAndZonesContains(String tenantId, String zone);
}
