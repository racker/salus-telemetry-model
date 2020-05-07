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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Replaces a field with the new value.
 * If the field didn't already exist it will be added.
 */
@Data @EqualsAndHashCode(callSuper = false)
public class ReplaceIntFieldValueTranslator extends MonitorTranslator {

  @NotEmpty
  String field;

  @NotNull
  Integer value;

  @Override
  public void translate(ObjectNode contentTree) {
    contentTree.put(field, value);
  }

  @Override
  public String info() {
    return String.format("The value of '%s' is set to %d", field, value);
  }
}
