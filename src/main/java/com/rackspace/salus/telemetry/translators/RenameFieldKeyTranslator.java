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
public class RenameFieldKeyTranslator extends MonitorTranslator {

  @NotEmpty
  String from;

  @NotEmpty
  String to;

  @Override
  public void translate(ObjectNode contentTree) {


    if (contentTree.hasNonNull(from)) {
      final JsonNode node = contentTree.remove(from);
      contentTree.set(to, node);
    } else {
      // if a null value is set we still remove the node since it does not have a valid key
      contentTree.remove(from);
      // and set a null node
      contentTree.putNull(to);
    }
  }

  @Override
  public String info() {
    return String.format("The key '%s' is renamed to '%s'", from, to);
  }
}
