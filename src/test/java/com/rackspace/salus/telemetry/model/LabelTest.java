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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class LabelTest {

  @Test
  public void convertingLabelListToMap() {
    List<Label> asList = new ArrayList<>();
    asList.add(new Label().setName("name1").setValue("value1"));
    asList.add(new Label().setName("name2").setValue("value2"));

    final Map<String, String> asMap = Label.convertToMap(asList);
    assertEquals(2, asMap.size());
    assertEquals("value1", asMap.get("name1"));
    assertEquals("value2", asMap.get("name2"));
  }

  @Test
  public void convertingMapToLabelList() {
    Map<String, String> asMap = new HashMap<>();
    asMap.put("name1", "value1");
    asMap.put("name2", "value2");

    final List<Label> asList = Label.convertToLabelList(asMap);
    assertEquals(2, asList.size());
    assertThat(asList, CoreMatchers.hasItems(
        new Label().setName("name1").setValue("value1"),
        new Label().setName("name2").setValue("value2")
    ));
  }
}