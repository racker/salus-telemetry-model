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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rackspace.salus.telemetry.errors.MonitorContentTranslationException;
import java.io.IOException;
import org.junit.Test;

public class JoinHostPortTranslatorTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testTranslate_numericPort() throws IOException, MonitorContentTranslationException {
    final String content = "{\"host\":\"somewhere\",\"port\":80}";

    final ObjectNode contentTree = (ObjectNode) objectMapper.readTree(content);

    final JoinHostPortTranslator translator = new JoinHostPortTranslator()
        .setFromHost("host")
        .setFromPort("port")
        .setTo("address");
    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);

    assertThat(contentTree.get("address")).isNotNull();
    assertThat(contentTree.get("address").asText()).isEqualTo("somewhere:80");
  }

  @Test
  public void testTranslate_stringPort() throws IOException, MonitorContentTranslationException {
    final String content = "{\"host\":\"somewhere\",\"port\":\"80\"}";

    final ObjectNode contentTree = (ObjectNode) objectMapper.readTree(content);

    final JoinHostPortTranslator translator = new JoinHostPortTranslator()
        .setFromHost("host")
        .setFromPort("port")
        .setTo("address");
    translator.translate(contentTree);

    assertThat(contentTree).hasSize(1);

    assertThat(contentTree.get("address")).isNotNull();
    // confirm same conversion as when port is a JSON numerical field
    assertThat(contentTree.get("address").asText()).isEqualTo("somewhere:80");
  }

  @Test
  public void testTranslate_missingPort() throws IOException {
    final String content = "{\"host\":\"somewhere\"}";

    final ObjectNode contentTree = (ObjectNode) objectMapper.readTree(content);

    final JoinHostPortTranslator translator = new JoinHostPortTranslator()
        .setFromHost("host")
        .setFromPort("port")
        .setTo("address");

    assertThatThrownBy(() -> translator.translate(contentTree))
        .isInstanceOf(MonitorContentTranslationException.class)
        .hasMessage("Both host and port must be set to use JoinHostPortTranslator");
  }
}