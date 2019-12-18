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
import java.util.Map;
import org.junit.Test;

public class ReplaceStringFieldValueTranslatorTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testTranslateReplaceExisting() {
    final Map<String,String> content = Map.of(
        "type", "dns",
        "field-not-match", "value"
    );

    final ReplaceStringFieldValueTranslator translator = new ReplaceStringFieldValueTranslator()
        .setField("type")
        .setValue("dns_query");

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(2);

    assertThat(contentTree.get("type")).isNotNull();
    assertThat(contentTree.get("type").asText()).isEqualTo("dns_query");

    assertThat(contentTree.get("field-not-match")).isNotNull();
    assertThat(contentTree.get("field-not-match").asText()).isEqualTo("value");
  }

  @Test
  public void testTranslateReplaceMissing() {
    final Map<String, String> content = Map.of(
        "field-not-match", "value"
    );

    final ReplaceStringFieldValueTranslator translator = new ReplaceStringFieldValueTranslator()
        .setField("new-field")
        .setValue("new-value");

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(2);

    assertThat(contentTree.get("new-field")).isNotNull();
    assertThat(contentTree.get("new-field").asText()).isEqualTo("new-value");

    assertThat(contentTree.get("field-not-match")).isNotNull();
    assertThat(contentTree.get("field-not-match").asText()).isEqualTo("value");
  }
}