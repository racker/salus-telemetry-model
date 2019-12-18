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
import java.util.Collections;
import java.util.Map;
import org.junit.Test;

public class ReplaceIntFieldValueTranslatorTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testTranslateReplaceExisting() {
    final Map<String,Integer> content = Map.of(
        "query_version", 1
    );

    final ReplaceIntFieldValueTranslator translator = new ReplaceIntFieldValueTranslator()
        .setField("query_version")
        .setValue(2);

    final ObjectNode contentTree = objectMapper.valueToTree(content);

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);

    assertThat(contentTree.get("query_version")).isNotNull();
    assertThat(contentTree.get("query_version").asInt()).isEqualTo(2);
  }

  @Test
  public void testTranslateReplaceMissing() {
    final ReplaceIntFieldValueTranslator translator = new ReplaceIntFieldValueTranslator()
        .setField("query_version")
        .setValue(3);

    final ObjectNode contentTree = objectMapper.valueToTree(Collections.emptyMap());

    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);

    assertThat(contentTree.get("query_version")).isNotNull();
    assertThat(contentTree.get("query_version").asInt()).isEqualTo(3);
  }
}