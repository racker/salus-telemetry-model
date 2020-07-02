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

package com.rackspace.salus.telemetry.validators;

import com.rackspace.salus.telemetry.model.CustomMetricExpression;
import com.rackspace.salus.telemetry.model.DerivativeNode;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomMetricListValidator implements
    ConstraintValidator<ValidCustomMetricList, List<CustomMetricExpression>> {

  @Override
  public boolean isValid(List<CustomMetricExpression> customMetrics,
      ConstraintValidatorContext constraintValidatorContext) {
    if (customMetrics == null) {
      return true;
    }

    long count = customMetrics.stream()
        .filter(DerivativeNode.class::isInstance)
        .count();

    return count <= 1;
  }
}
