/*
 * Copyright 2020 Rackspace US, Inc.
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
 *
 */
package com.rackspace.salus.telemetry.repositories;

import com.rackspace.salus.telemetry.entities.Zone;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ZoneRepository extends PagingAndSortingRepository<Zone, UUID> {
    Optional<Zone> findByTenantIdAndName(String tenantId, String name);

    Page<Zone> findAllByTenantId(String tenantId, Pageable page);

    @Query(
        "select z from Zone z where z.tenantId = :tenantId"
        + " or z.tenantId = com.rackspace.salus.telemetry.entities.Zone.PUBLIC"
            + " order by FIELD(tenantId, com.rackspace.salus.telemetry.entities.Zone.PUBLIC) DESC"
            + ", name ASC")
    Page<Zone> findAllAvailableForTenant(String tenantId, Pageable page);

    @Query("select z.name from Zone z where z.tenantId = :tenantId")
    Page<String> findAllZoneNameForTenant(String tenantId, Pageable page);

    boolean existsByTenantIdAndName(String tenantId, String name);

    @Transactional
    void deleteAllByTenantId(String tenantId);
}