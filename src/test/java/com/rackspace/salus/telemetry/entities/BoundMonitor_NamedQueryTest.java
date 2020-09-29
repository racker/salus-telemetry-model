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

package com.rackspace.salus.telemetry.entities;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

import com.rackspace.salus.telemetry.EnableSalusJpa;
import com.rackspace.salus.telemetry.model.AgentType;
import com.rackspace.salus.telemetry.model.ConfigSelectorScope;
import com.rackspace.salus.telemetry.model.MonitorType;
import com.rackspace.salus.telemetry.repositories.BoundMonitorRepository;
import com.rackspace.salus.test.EnableTestContainersDatabase;
import java.time.Duration;
import java.util.List;
import javax.persistence.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableTestContainersDatabase
@DataJpaTest(showSql = false)
public class BoundMonitor_NamedQueryTest {

  @Configuration
  @EnableSalusJpa
  static class TestConfiguration {

  }

  @Autowired
  BoundMonitorRepository boundMonitorRepository;

  @Autowired
  TestEntityManager entityManager;

  @Test
  public void testGetLeastLoadedPublicPoller() {
    final String zoneName = randomAlphanumeric(10);
    final String poller1 = randomAlphanumeric(10);
    final String poller2 = randomAlphanumeric(10);
    final String poller3 = randomAlphanumeric(10);
    createRemoteBindings(15, zoneName, poller1, randomAlphanumeric(10));
    createRemoteBindings(10, zoneName, poller2, randomAlphanumeric(10));
    createRemoteBindings(20, zoneName, poller3, randomAlphanumeric(10));

    final Tuple result = entityManager.getEntityManager()
        .createNamedQuery("BoundMonitor.publicPollerLoading", Tuple.class)
        .setParameter("zoneName", zoneName)
        .setMaxResults(1)
        .getSingleResult();

    assertThat(result.get(0)).isEqualTo(poller2);
    assertThat(result.get(1)).isEqualTo(10L);
  }

  @Test
  public void testGetLeastLoadedPublicPoller_nullsExcluded() {
    final String zoneName = randomAlphanumeric(10);
    final String poller = randomAlphanumeric(5);
    createRemoteBindings(3, zoneName, poller, randomAlphanumeric(10));
    createRemoteBindings(2, zoneName, null, randomAlphanumeric(10));

    final List<Tuple> result = entityManager.getEntityManager()
        .createNamedQuery("BoundMonitor.publicPollerLoading", Tuple.class)
        .setParameter("zoneName", zoneName)
        .getResultList();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).get("pollerResourceId")).isEqualTo(poller);
    assertThat(result.get(0).get("load")).isEqualTo(3L);
  }

  @Test
  public void testGetLeastLoadedPrivatePoller() {
    final String tenantId = randomAlphanumeric(10);
    final String zoneName = randomAlphanumeric(10);
    final String poller1 = randomAlphanumeric(10);
    final String poller2 = randomAlphanumeric(10);
    final String poller3 = randomAlphanumeric(10);
    createRemoteBindings(15, zoneName, poller1, tenantId);
    createRemoteBindings(10, zoneName, poller2, tenantId);
    createRemoteBindings(20, zoneName, poller3, tenantId);

    final Tuple result = entityManager.getEntityManager()
        .createNamedQuery("BoundMonitor.privatePollerLoading", Tuple.class)
        .setParameter("tenantId", tenantId)
        .setParameter("zoneName", zoneName)
        .setMaxResults(1)
        .getSingleResult();

    assertThat(result.get(0)).isEqualTo(poller2);
    assertThat(result.get(1)).isEqualTo(10L);
  }

  @Test
  public void testGetLeastLoadedPrivatePoller_nullsExcluded() {
    final String tenantId = randomAlphanumeric(10);
    final String zoneName = randomAlphanumeric(10);
    final String poller = randomAlphanumeric(5);
    createRemoteBindings(3, zoneName, poller, tenantId);
    createRemoteBindings(2, zoneName, null, tenantId);

    final List<Tuple> result = entityManager.getEntityManager()
        .createNamedQuery("BoundMonitor.privatePollerLoading", Tuple.class)
        .setParameter("tenantId", tenantId)
        .setParameter("zoneName", zoneName)
        .getResultList();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).get("pollerResourceId")).isEqualTo(poller);
    assertThat(result.get(0).get("load")).isEqualTo(3L);
  }

  private void createRemoteBindings(int count, String zoneName, String pollerResourceId,
                                    String tenantId) {
    final Monitor monitor = createMonitor(tenantId, ConfigSelectorScope.REMOTE, MonitorType.http);
    for (int i = 0; i < count; i++) {
      entityManager.persist(
          new BoundMonitor()
              .setMonitor(monitor)
              .setTenantId(tenantId)
              .setZoneName(zoneName)
              .setResourceId(randomAlphanumeric(10))
              .setEnvoyId(pollerResourceId != null ? "e-"+pollerResourceId : null)
              .setPollerResourceId(pollerResourceId)
              .setRenderedContent("{}")
      );
    }
  }

  private Monitor createMonitor(String monitorTenant,
                                ConfigSelectorScope selectorScope, MonitorType monitorType) {
    return entityManager.persist(new Monitor()
        .setAgentType(AgentType.TELEGRAF)
        .setSelectorScope(selectorScope)
        .setContent("{}")
        .setTenantId(monitorTenant)
        .setMonitorType(monitorType)
        .setInterval(Duration.ofSeconds(60))
    );
  }

}
