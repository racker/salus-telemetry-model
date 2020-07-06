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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rackspace.salus.telemetry.translators.GoDurationTranslator;
import java.time.Duration;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * This class represents a DerivativeNode to be used in TickScript.
 *
 * It is used to generate a new metric containing the rate of change of one metric over a
 * certain duration.
 *
 * TickScript computes the derivative via: (current - previous ) / ( time_difference / unit)
 *
 */
@Data
public class DerivativeNode extends MetricExpressionBase {
  @NotBlank
  String metric;
  Duration duration = Duration.ofMinutes(1);
  @NotBlank
  String as;
  boolean nonNegative;

  /**
   * @return The duration value in the go format used by TickScript.
   */
  @JsonIgnore
  public String getGoDuration() {
    return GoDurationTranslator.getGoDuration(this.duration);
  }
}