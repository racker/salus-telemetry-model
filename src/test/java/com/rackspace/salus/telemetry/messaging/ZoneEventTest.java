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

package com.rackspace.salus.telemetry.messaging;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@JsonTest
public class ZoneEventTest {
  // disable application context loading
  @Configuration
  public static class TestConfig{ }

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JacksonTester<ZoneEvent> json;

  @Test
  public void testDeserialize_NewResourceZoneEvent() throws IOException {
    final String content =
        TestUtils.loadContent("/ZoneEventTest/resourceZoneEvent.json");

    final ZoneEvent event = json.parseObject(content);
    assertThat(event, instanceOf(NewResourceZoneEvent.class));

    final NewResourceZoneEvent casted = (NewResourceZoneEvent) event;
    assertThat(casted.getZoneName(), equalTo("z-1"));
    assertThat(casted.getTenantId(), equalTo("t-1"));
  }

  @Test
  public void testDeserialize_ReattachedResourceZoneEvent() throws IOException {
    final String content =
        TestUtils.loadContent("/ZoneEventTest/reattachedResourceZoneEvent.json");

    final ZoneEvent event = json.parseObject(content);
    assertThat(event, instanceOf(ReattachedResourceZoneEvent.class));

    final ReattachedResourceZoneEvent casted = (ReattachedResourceZoneEvent) event;
    assertThat(casted.getZoneName(), equalTo("z-1"));
    assertThat(casted.getTenantId(), equalTo("t-1"));
    assertThat(casted.getFromEnvoyId(), equalTo("e-1"));
    assertThat(casted.getToEnvoyId(), equalTo("e-2"));
  }

}