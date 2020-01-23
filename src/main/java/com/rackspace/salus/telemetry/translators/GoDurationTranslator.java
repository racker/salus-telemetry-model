/*
 * Copyright 2020 Rackspace US, Inc.
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
import java.time.Duration;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * It will convert the provided field from a Java Duration to the format expected
 * by Go.  For example, "PT20S" will be converted to "20s".
 * If the value is null, no action will be taken.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoDurationTranslator extends MonitorTranslator {
  @NotEmpty
  String field;

  @Override
  public void translate(ObjectNode contentTree) {

    if (contentTree.hasNonNull(field)) {
      final JsonNode node = contentTree.remove(field);
      String nodes = node.asText();
      Duration duration = Duration.parse(nodes);
      if (duration.toSecondsPart() == 0) {
        contentTree.put(field, String.format("%dm", duration.toMinutes()));
      } else {
        contentTree.put(field, String.format("%ds", duration.toSeconds()));
      }
    }
  }
}
