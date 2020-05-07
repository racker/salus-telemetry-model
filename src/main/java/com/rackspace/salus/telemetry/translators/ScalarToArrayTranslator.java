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
 * Takes the value of a scalar field (<code>from</code>) and creates an array at
 * <code>to</code> containing that value. <code>from</code> can be the same as <code>to</code> in
 * order to perform an "in place" translation. If the <code>from</code> field is absent, nothing
 * is changed.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ScalarToArrayTranslator extends MonitorTranslator {
  @NotEmpty
  String from;

  @NotEmpty
  String to;

  @Override
  public void translate(ObjectNode contentTree) {
    if (contentTree.hasNonNull(from)) {
      final JsonNode node = contentTree.remove(from);
      contentTree.putArray(to).add(node);
    } else {
      // if a null value is set we still remove the node since it does not have a valid key
      contentTree.remove(from);
      // and set an empty array node
      contentTree.putArray(to);
    }
  }

  @Override
  public String info() {
    if (from.equals(to)) {
      return String.format("'%s' becomes a singleton array", from);
    } else {
      return String.format("'%s' becomes the singleton array named '%s'", from, to);
    }
  }
}
