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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LabelNamespacesTest {

  @Test
  public void testValidateUserLabel_valid() {
    assertThat(
        LabelNamespaces.validateUserLabel("totallyMyCustomLabel"),
        is(true)
    );
  }

  @Test
  public void testValidateUserLabel_invalid() {
    assertThat(
        LabelNamespaces.validateUserLabel(LabelNamespaces.AGENT + "_os"),
        is(false)
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateUserLabel_null() {
    LabelNamespaces.validateUserLabel(null);
  }

  @Test
  public void testApplyNamespace() {
    final String result = LabelNamespaces.applyNamespace("ns", "name");
    assertThat(result, equalTo("ns_name"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyNamespace_badNamespace() {
    LabelNamespaces.applyNamespace(null, "nmame");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyNamespace_nullLabel() {
    LabelNamespaces.applyNamespace("ns", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyNamespace_emptyLabel() {
    LabelNamespaces.applyNamespace("ns", "");
  }

  @Test
  public void testHasNamespace() {
    assertThat(
        LabelNamespaces.labelHasNamespace("agent_env", "agent"),
        equalTo(true)
    );
    assertThat(
        LabelNamespaces.labelHasNamespace("customOne", "agent"),
        equalTo(false)
    );
  }
}