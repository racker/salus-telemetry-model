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

package com.rackspace.salus.telemetry.model;

import lombok.Data;

/**
 * A custom EvalNode implementation to provide the ability to generate a percentage of two metrics.
 *
 * e.g. disk metrics could be provided for `used` and `total` to then provide a metric containing
 * the utilization percentage value.
 */
@Data
public class PercentageEvalNode extends EvalNode {
  String x;
  String y;

  @Override
  public String getLambda() {
    return String.format("lambda: (float(\"%s\") / float(\"%s\")) * 100.0", x, y);
  }
}
