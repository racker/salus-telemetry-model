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

package com.rackspace.salus.telemetry.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidLabelKeysValidatorTest {

  private LocalValidatorFactoryBean validatorFactoryBean;

  static class DataWithLabels {
    @ValidLabelKeys
    Map<String,String> labels;
  }

  @Before
  public void setUp() {
    validatorFactoryBean = new LocalValidatorFactoryBean();
    validatorFactoryBean.afterPropertiesSet();
  }

  @Test
  public void testValidation_normal() {
    Map<String, String> labels = new HashMap<>();
    labels.put("simple", "value");
    labels.put("agent_discovered_os", "linux");
    labels.put("simple2", "value2");

    final Resource resource = new Resource()
        .setLabels(labels)
        .setTenantId("t-1")
        .setResourceId("r-1")
        .setPresenceMonitoringEnabled(true);
    final Set<ConstraintViolation<Resource>> results = validatorFactoryBean.validate(resource);

    assertThat(results, equalTo(Collections.emptySet()));
  }

  @Test
  public void testWithNullField() {
    final DataWithLabels obj = new DataWithLabels();

    final Set<ConstraintViolation<DataWithLabels>> results = validatorFactoryBean.validate(obj);

    assertThat(results, equalTo(Collections.emptySet()));
  }

  @Test
  public void testValidation_dots() {
    failingScenario("agent.discovered.os");
  }

  @Test
  public void testValidation_leadingDigit() {
    validScenario("1_dfw");
  }

  @Test
  public void testValidation_leadingUnderscore() {
    validScenario("_this_is_a_test");
  }

  @Test
  public void testValidation_invalidKey_blank() {
    failingScenario("");
  }

  private void validScenario(String labelKey) {
    Map<String, String> labels = new HashMap<>();
    labels.put(labelKey, "linux");

    final Resource resource = new Resource()
        .setLabels(labels)
        .setTenantId("t-1")
        .setResourceId("r-1")
        .setPresenceMonitoringEnabled(true);
    final Set<ConstraintViolation<Resource>> results = validatorFactoryBean.validate(resource);

    assertThat(results.size(), equalTo(0));
  }

  private void failingScenario(String labelKey) {
    Map<String, String> labels = new HashMap<>();
    labels.put(labelKey, "linux");

    final Resource resource = new Resource()
        .setLabels(labels)
        .setTenantId("t-1")
        .setResourceId("r-1")
        .setPresenceMonitoringEnabled(true);
    final Set<ConstraintViolation<Resource>> results = validatorFactoryBean.validate(resource);

    assertThat(results.size(), equalTo(1));
    final ConstraintViolation<Resource> violation = results.iterator().next();
    assertThat(violation.getPropertyPath().toString(), equalTo("labels"));
    assertThat(violation.getMessage(), equalTo("All label names must consist of alpha-numeric or underscore characters"));
  }
}