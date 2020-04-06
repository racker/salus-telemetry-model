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
import com.rackspace.salus.telemetry.model.LabelSelectorMethod;
import com.rackspace.salus.telemetry.model.MonitorType;
import com.rackspace.salus.telemetry.repositories.MonitorRepository;
import com.rackspace.salus.test.EnableTestContainersDatabase;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableTestContainersDatabase
@DataJpaTest(showSql = false)
public class Monitor_NamedQueryTest {

  @Configuration
  @EnableSalusJpa
  static class TestConfiguration {
  }

  @Autowired
  MonitorRepository monitorRepository;

  @Autowired
  EntityManager entityManager;

  final int tenantsMonitorMetadata1 = new Random().nextInt(10) + 2;
  final int tenantsMonitorMetadata2 = new Random().nextInt(10) + 2;
  final int tenantsPluginMetadata1 = new Random().nextInt(10) + 2;
  final int tenantsPluginMetadata2 = new Random().nextInt(10) + 2;
  final int tenantsPolicyZone = new Random().nextInt(10) + 2;
  final int tenantsLocal = new Random().nextInt(10) + 2;

  @Before
  public void setup() {
    // populate tenants using monitor metadata
    saveMonitorsForTenantWithMetadata(tenantsMonitorMetadata1, List.of("monitorKey1"), Collections.emptyList());
    saveMonitorsForTenantWithMetadata(tenantsMonitorMetadata2, List.of("monitorKey2"), Collections.emptyList());
    // populate tenants using plugin metadata
    saveMonitorsForTenantWithMetadata(tenantsPluginMetadata1, Collections.emptyList(), List.of("pluginKey1"));
    saveMonitorsForTenantWithMetadata(tenantsPluginMetadata2, Collections.emptyList(), List.of("pluginKey2"));
    // populate tenants using monitor metadata
    saveMonitorsForTenantsWithZoneMetadata(tenantsPolicyZone);
    // populate tenants using local monitors
    saveLocalMonitors(tenantsLocal);
  }

  @Test
  public void testGetTenantsUsingPolicyMetadataInMonitor() {
    List<String> tenantsUsingPolicyKey = entityManager
        .createNamedQuery("Monitor.getTenantsUsingPolicyMetadataInMonitor", String.class)
        .setParameter("metadataKey", "monitorKey1")
        .getResultList();

    assertThat(tenantsUsingPolicyKey).hasSize(tenantsMonitorMetadata1);
  }

  @Test
  public void testGetTenantsUsingPolicyMetadataInPlugin() {
    List<String> tenantsUsingPolicyKey = entityManager
        .createNamedQuery("Monitor.getTenantsUsingPolicyMetadataInPlugin", String.class)
        .setParameter("metadataKey", "pluginKey1")
        .getResultList();

    assertThat(tenantsUsingPolicyKey).hasSize(tenantsPluginMetadata1);
  }

  @Test
  public void testGetTenantsUsingZoneMetadata() {
    List<String> tenantsUsingZonePolicy = entityManager
        .createNamedQuery("Monitor.getTenantsUsingZoneMetadata", String.class)
        .getResultList();

    assertThat(tenantsUsingZonePolicy).hasSize(tenantsPolicyZone);
  }

  @Test
  public void testGetTenantsUsingZoneMetadata_moreTenants() {
    // add more tenants and verify the result is still accurate
    saveMonitorsForTenantsWithZoneMetadata(5);

    List<String> tenantsUsingZonePolicy = entityManager
        .createNamedQuery("Monitor.getTenantsUsingZoneMetadata", String.class)
        .getResultList();

    assertThat(tenantsUsingZonePolicy).hasSize(tenantsPolicyZone + 5);
  }

  private void saveMonitorsForTenantWithMetadata(int tenants, List<String> monitorMetadata, List<String> pluginMetadata) {
    // create a random number of monitors per tenant (at least 2)
    int monitorsPerTenant = new Random().nextInt(10) + 2;

    for (int i = 0; i < tenants; i++) {
      String tenantId = randomAlphanumeric(5);
      for (int j = 0; j < monitorsPerTenant; j++) {
        monitorRepository.save(new Monitor()
            .setAgentType(AgentType.TELEGRAF)
            .setMonitorType(MonitorType.ping)
            .setContent("original content")
            .setTenantId(tenantId)
            .setSelectorScope(ConfigSelectorScope.REMOTE)
            .setZones(Collections.singletonList("z-1"))
            .setLabelSelector(Collections.singletonMap("os", "linux"))
            .setLabelSelectorMethod(LabelSelectorMethod.OR)
            .setInterval(Duration.ofSeconds(60))
            .setMonitorMetadataFields(monitorMetadata)
            .setPluginMetadataFields(pluginMetadata));
      }
    }
  }

  private void saveMonitorsForTenantsWithZoneMetadata(int tenants) {
    // create a random number of monitors per tenant (at least 2)
    int monitorsPerTenant = new Random().nextInt(10) + 2;

    for (int i = 0; i < tenants; i++) {
      String tenantId = randomAlphanumeric(5);
      for (int j = 0; j < monitorsPerTenant; j++) {
        monitorRepository.save(new Monitor()
            .setAgentType(AgentType.TELEGRAF)
            .setMonitorType(MonitorType.ping)
            .setContent("original content")
            .setTenantId(tenantId)
            .setSelectorScope(ConfigSelectorScope.REMOTE)
            .setZones(Collections.emptyList())
            .setLabelSelector(Collections.singletonMap("os", "linux"))
            .setLabelSelectorMethod(LabelSelectorMethod.OR)
            .setInterval(Duration.ofSeconds(60))
            .setMonitorMetadataFields(Collections.emptyList())
            .setPluginMetadataFields(Collections.emptyList()));
      }
    }
  }

  private void saveLocalMonitors(int tenants) {
    // create a random number of monitors per tenant (at least 2)
    int monitorsPerTenant = new Random().nextInt(10) + 2;

    for (int i = 0; i < tenants; i++) {
      String tenantId = randomAlphanumeric(5);
      for (int j = 0; j < monitorsPerTenant; j++) {
        monitorRepository.save(new Monitor()
            .setAgentType(AgentType.TELEGRAF)
            .setMonitorType(MonitorType.cpu)
            .setContent("original content")
            .setTenantId(tenantId)
            .setSelectorScope(ConfigSelectorScope.LOCAL)
            .setZones(Collections.emptyList())
            .setLabelSelector(Collections.singletonMap("os", "linux"))
            .setLabelSelectorMethod(LabelSelectorMethod.OR)
            .setInterval(Duration.ofSeconds(60))
            .setMonitorMetadataFields(Collections.emptyList())
            .setPluginMetadataFields(Collections.emptyList()));
      }
    }
  }
}
