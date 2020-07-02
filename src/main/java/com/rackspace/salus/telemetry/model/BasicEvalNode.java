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
import com.rackspace.salus.telemetry.validators.BasicEvalNodeValidator;
import com.rackspace.salus.telemetry.validators.ValidBasicEvalNode;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * An EvalNode implementation that allows the use of all known TickScript functions along with
 * a single operator.
 *
 * If more complex eval expressions are required a custom class should be created.
 */
@Data
@ValidBasicEvalNode
public class BasicEvalNode extends EvalNode {
  @NotEmpty
  List<String> operands;
  @NotBlank
  String operator;

  @JsonIgnore
  final Pattern evalExpression = Pattern.compile(BasicEvalNodeValidator.functionRegex);
  @JsonIgnore
  final Pattern validRealNumber = Pattern.compile("^[-+]?([0-9]+(\\.[0-9]+)?|\\.[0-9]+)$");

  @Override
  public String getLambda() {
    List<String> normalizedOperands = this.getOperands().stream()
        .map(this::normalize)
        .collect(Collectors.toList());

    return "lambda: " + normalizedOperands.stream()
        .collect(Collectors.joining(" " + this.getOperator() + " "));
  }

  private boolean isValidRealNumber(String operand) {
    return validRealNumber.matcher(operand).matches();
  }

  private String normalize(String operand) {
    if (isValidRealNumber(operand)) {
      return operand;
    }

    Matcher matcher = evalExpression.matcher(operand);

    //  if operand is not a function call, double quote it
    if (!matcher.matches()) {
      // operand doesn't contain function, and thus is a tag/field name requiring double quotes
      return "\"" + operand + "\"";
    }

    // Operand is function call, so split out the function parameters, double quoting the tag/fields
    String parameters = Arrays.stream(matcher.group(2).split(","))
        .map(String::trim)
        .map(p -> isValidRealNumber(p) ? p : "\"" + p + "\"")
        .collect(Collectors.joining(", "));

    return matcher.group(1) + "(" + parameters + ")";
  }
}