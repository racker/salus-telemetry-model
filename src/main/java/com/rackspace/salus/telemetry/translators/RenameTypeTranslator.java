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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rackspace.salus.telemetry.errors.MonitorContentTranslationException;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Replaced the monitor type with the new value provided.
 * If the field didn't already exist it will be added.
 */
@Data @EqualsAndHashCode(callSuper = false)
public class RenameTypeTranslator extends MonitorTranslator {

  @NotEmpty
  String value;

  @Override
  public void translate(ObjectNode contentTree) throws MonitorContentTranslationException {
    if (!contentTree.has(MonitorTranslator.TYPE_PROPERTY)) {
      throw new MonitorContentTranslationException(
          "Cannot set type on monitor that has no existing type.");
    }
    contentTree.put(MonitorTranslator.TYPE_PROPERTY, value);
  }
}
