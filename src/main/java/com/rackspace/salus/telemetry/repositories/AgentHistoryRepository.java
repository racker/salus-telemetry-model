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
 */

package com.rackspace.salus.telemetry.repositories;

import com.rackspace.salus.telemetry.entities.AgentHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AgentHistoryRepository extends CrudRepository<AgentHistory, UUID> {

  @Query("select a from AgentHistory a where a.envoyID = :envoyId "
      + "and a.connectedClosedAt IS NULL ORDER BY a.connectedAt DESC")
  List<AgentHistory> findAllByEnvoyIdAAndConnectedClosedAtNull(String envoyId);
}
