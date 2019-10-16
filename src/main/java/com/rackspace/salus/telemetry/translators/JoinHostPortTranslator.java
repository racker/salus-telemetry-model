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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Takes two distinct host and port fields, replaces, and joins them into a combined
 * "host:port" field.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JoinHostPortTranslator extends MonitorTranslator {

  @NotEmpty
  String fromHost;

  @NotEmpty
  String fromPort;

  @NotEmpty
  String to;

  @Override
  public void translate(ObjectNode contentTree) {
    final JsonNode hostNode = contentTree.get(fromHost);
    final JsonNode portNode = contentTree.get(fromPort);

    if (hostNode != null && portNode != null) {
      // only remove when both present
      contentTree.remove(fromHost);
      contentTree.remove(fromPort);

      contentTree.put(to, String.join(":", hostNode.asText(), portNode.asText()));
    }
  }
}
