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
 * Renames a field, if present, to a new name with the same value.
 */
@Data @EqualsAndHashCode(callSuper = false)
public class RenameFieldTranslator extends MonitorTranslator {

  @NotEmpty
  String from;

  @NotEmpty
  String to;

  @Override
  public void translate(ObjectNode contentTree) {

    final JsonNode node = contentTree.remove(from);
    if (node != null) {
      contentTree.set(to, node);
    }
  }
}
