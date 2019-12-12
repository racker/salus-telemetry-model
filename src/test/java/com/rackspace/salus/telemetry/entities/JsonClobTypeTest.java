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

package com.rackspace.salus.telemetry.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.rackspace.salus.telemetry.EnableSalusJpa;
import com.rackspace.salus.telemetry.model.AgentType;
import com.rackspace.salus.telemetry.model.ConfigSelectorScope;
import com.rackspace.salus.telemetry.model.MonitorType;
import com.rackspace.salus.telemetry.translators.RenameFieldTranslator;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureJson // since some app components need ObjectMapper
public class JsonClobTypeTest {
  @Configuration
  @EnableSalusJpa
  static class TestConfiguration {
  }

  @Autowired
  TestEntityManager entityManager;

  @Test
  public void testPersistRoundtrip() {
    final MonitorTranslationOperator operator = new MonitorTranslationOperator()
        .setName("cpu-rename")
        .setAgentType(AgentType.TELEGRAF)
        .setAgentVersions(">= 1.12.0")
        .setMonitorType(MonitorType.cpu)
        .setSelectorScope(ConfigSelectorScope.LOCAL)
        .setTranslatorSpec(new RenameFieldTranslator()
            .setFrom("from-field")
            .setTo("to-field")
        );

    final MonitorTranslationOperator saved =
        entityManager.persistFlushFind(operator);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved).isEqualToIgnoringGivenFields(operator, "id");
  }

  @Test
  public void testNullClobValue() {
    final Resource resource = new Resource()
        .setTenantId("t-1")
        .setResourceId("r-1")
        .setLabels(Map.of())
        .setPresenceMonitoringEnabled(true);

    final Resource saved =
        entityManager.persistFlushFind(resource);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved).isEqualToIgnoringGivenFields(resource, "id");
  }
}