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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Map;
import org.junit.Test;

public class ScalarToArrayTranslatorTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testTranslate() {
    final Map<String,String> content = Map.of(
        "field-match", "value-match",
        "field-not-match", "value-not-match"
    );

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    final ScalarToArrayTranslator translator = new ScalarToArrayTranslator()
        .setFrom("field-match")
        .setTo("now-an-array");

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(2);

    assertThat(contentTree.get("field-match")).isNull();

    assertThat(contentTree.get("now-an-array")).isNotNull();
    assertThat(contentTree.get("now-an-array").isArray()).isTrue();
    assertThat(contentTree.get("now-an-array")).hasSize(1);
    assertThat(contentTree.get("now-an-array").get(0).asText()).isEqualTo("value-match");

    assertThat(contentTree.get("field-not-match")).isNotNull();
    assertThat(contentTree.get("field-not-match").asText()).isEqualTo("value-not-match");
  }

  @Test
  public void testTranslate_missingFrom() {
    final Map<String,String> content = Map.of(
        "field-not-match", "value-not-match"
    );

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    final ScalarToArrayTranslator translator = new ScalarToArrayTranslator()
        .setFrom("field-match")
        .setTo("now-an-array");

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(2);

    assertThat(contentTree.get("field-not-match")).isNotNull();
    assertThat(contentTree.get("field-not-match").asText()).isEqualTo("value-not-match");
    assertThat(contentTree.get("now-an-array")).hasSize(0);
  }

  @Test
  public void testTranslate_nullValue() throws IOException {
    // We cannot use a map to test null due to https://github.com/FasterXML/jackson-databind/issues/2430
    String content = "{\"field-match\": null}";

    final ObjectNode contentTree = (ObjectNode) objectMapper.readTree(content);

    final ScalarToArrayTranslator translator = new ScalarToArrayTranslator()
        .setFrom("field-match")
        .setTo("now-an-array");

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);
    assertThat(contentTree.get("field-match")).isNull();
    assertThat(contentTree.get("now-an-array")).isNotNull();
    assertThat(contentTree.get("now-an-array")).isInstanceOf(ArrayNode.class);
    assertThat(contentTree.get("now-an-array")).hasSize(0);
  }
}