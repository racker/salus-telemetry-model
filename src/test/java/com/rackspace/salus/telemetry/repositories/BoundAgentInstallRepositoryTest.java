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


import static org.assertj.core.api.Assertions.assertThat;

import com.rackspace.salus.telemetry.entities.AgentInstall;
import com.rackspace.salus.telemetry.entities.AgentRelease;
import com.rackspace.salus.telemetry.entities.BoundAgentInstall;
import com.rackspace.salus.telemetry.model.AgentType;
import com.rackspace.salus.telemetry.model.LabelSelectorMethod;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BoundAgentInstallRepositoryTest {

  @Configuration
  @EntityScan("com.rackspace.salus.telemetry.entities")
  @EnableJpaRepositories("com.rackspace.salus.telemetry.repositories")
  static class TestConfiguration {
  }

  @Autowired
  BoundAgentInstallRepository repository;

  @Autowired
  TestEntityManager em;

  @Test
  public void findAllByTenantResourceAgentType() {
    final AgentRelease release = saveRelease("1.0.0", AgentType.TELEGRAF);

    final AgentInstall install = saveInstall(release, Collections.singletonMap("os","linux"), LabelSelectorMethod.AND);

    final BoundAgentInstall binding1 = saveBinding(install, "r-1");
    // extra entry to verify filtering
    saveBinding(install, "r-2");

    final List<BoundAgentInstall> results = repository
        .findAllByTenantResourceAgentType("t-1", "r-1", AgentType.TELEGRAF);

    assertThat(results).containsExactlyInAnyOrder(
        binding1
    );
  }

  @Test
  public void findAllByTenantResourceAgentTypeUsingOr() {
    final AgentRelease release = saveRelease("1.0.0", AgentType.TELEGRAF);

    final AgentInstall install = saveInstall(release, Collections.singletonMap("os","linux"), LabelSelectorMethod.OR);

    final BoundAgentInstall binding1 = saveBinding(install, "r-1");
    // extra entry to verify filtering
    saveBinding(install, "r-2");

    final List<BoundAgentInstall> results = repository
        .findAllByTenantResourceAgentType("t-1", "r-1", AgentType.TELEGRAF);

    assertThat(results).containsExactlyInAnyOrder(
        binding1
    );
  }

  @Test
  public void testFindAllByTenantResource() {
    final AgentRelease releaseT = saveRelease("1.0.0", AgentType.TELEGRAF);
    final AgentRelease releaseF = saveRelease("1.1.1", AgentType.FILEBEAT);

    final AgentInstall installT = saveInstall(
        releaseT, Collections.singletonMap("os", "linux"), LabelSelectorMethod.AND);
    final AgentInstall installF = saveInstall(
        releaseF, Collections.singletonMap("os", "linux"), LabelSelectorMethod.AND);

    saveBinding(installT, "r-both");
    saveBinding(installF, "r-both");

    saveBinding(installT, "r-one");

    {
      final List<BoundAgentInstall> bindings = repository
          .findAllByTenantResource("t-1", "r-both");

      assertThat(bindings).containsExactlyInAnyOrder(
          new BoundAgentInstall().setAgentInstall(installT).setResourceId("r-both"),
          new BoundAgentInstall().setAgentInstall(installF).setResourceId("r-both")
      );
    }

    {
      final List<BoundAgentInstall> bindings = repository
          .findAllByTenantResource("t-1", "r-one");

      assertThat(bindings).containsExactlyInAnyOrder(
          new BoundAgentInstall().setAgentInstall(installT).setResourceId("r-one")
      );
    }

    {
      final List<BoundAgentInstall> bindings = repository
          .findAllByTenantResource("t-1", "r-none");

      assertThat(bindings).isEmpty();
    }

    {
      final List<BoundAgentInstall> bindings = repository
          .findAllByTenantResource("t-other", "r-both");

      assertThat(bindings).isEmpty();
    }
  }

  private AgentRelease saveRelease(String version, AgentType agentType) {
    return em.persistAndFlush(
          new AgentRelease()
              .setType(agentType)
              .setVersion(version)
              .setUrl("")
              .setExe("")
      );
  }

  private BoundAgentInstall saveBinding(AgentInstall agentInstall, String resourceId) {
    return em.persistAndFlush(
          new BoundAgentInstall()
              .setAgentInstall(agentInstall)
              .setResourceId(resourceId)
      );
  }

  private AgentInstall saveInstall(AgentRelease agentRelease, Map<String, String> installSelector, LabelSelectorMethod labelSelectorMethod) {
    return em.persistAndFlush(
          new AgentInstall()
              .setAgentRelease(agentRelease)
              .setTenantId("t-1")
              .setLabelSelector(installSelector)
              .setLabelSelectorMethod(labelSelectorMethod)
      );
  }
}