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

package com.rackspace.salus.telemetry.translators;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Map;
import org.junit.Test;

public class DurationInSecondsTranslatorTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testTranslate_seconds() {
    final Map<String,String> content = Map.of(
        "timeout", "PT30S"
    );

    final DurationInSecondsTranslator translator = new DurationInSecondsTranslator()
        .setField("timeout");

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);

    assertThat(contentTree.get("timeout")).isNotNull();
    assertThat(contentTree.get("timeout").asText()).isEqualTo("30");
  }

  @Test
  public void testTranslate_mins() {
    final Map<String,String> content = Map.of(
        "timeout", "PT3M"
    );

    final DurationInSecondsTranslator translator = new DurationInSecondsTranslator()
        .setField("timeout");

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);

    assertThat(contentTree.get("timeout")).isNotNull();
    assertThat(contentTree.get("timeout").asText()).isEqualTo("180");
  }

  @Test
  public void testTranslate_mins_and_secs() {
    final Map<String,String> content = Map.of(
        "timeout", "PT1M30S"
    );

    final DurationInSecondsTranslator translator = new DurationInSecondsTranslator()
        .setField("timeout");

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);

    assertThat(contentTree.get("timeout")).isNotNull();
    assertThat(contentTree.get("timeout").asText()).isEqualTo("90");
  }

  @Test
  public void testTranslate_null() throws IOException {
    // We cannot use a map to test null due to https://github.com/FasterXML/jackson-databind/issues/2430
    String content = "{\"timeout\": null}";

    final DurationInSecondsTranslator translator = new DurationInSecondsTranslator()
        .setField("timeout");

    final ObjectNode contentTree = (ObjectNode) objectMapper.readTree(content);

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);
    assertThat(contentTree.get("timeout")).isNotNull();
  }
}