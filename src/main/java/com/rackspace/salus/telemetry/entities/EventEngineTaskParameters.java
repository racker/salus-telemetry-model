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

package com.rackspace.salus.telemetry.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.rackspace.salus.telemetry.model.ValidLabelKeys;
import com.rackspace.salus.telemetry.validators.EvalExpressionValidator.EvalExpressionValidation;
import com.rackspace.salus.telemetry.validators.ExpressionValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class EventEngineTaskParameters {

  @Min(1)
  @Max(10)
  Integer criticalStateDuration;

  @Min(1)
  @Max(10)
  Integer warningStateDuration;

  @Min(1)
  @Max(10)
  Integer infoStateDuration;

  @NotEmpty
  List<StateExpression> stateExpressions = new ArrayList<>();

  @Valid
  List<EvalExpression> evalExpressions;

  Integer windowLength;
  List<String> windowFields;

  boolean flappingDetection;

  @ValidLabelKeys
  Map<String, String> labelSelector;

  @Data
  @EvalExpressionValidation
  public static class EvalExpression {
    @NotEmpty
    List<String> operands;
    @NotBlank
    String operator;
    @NotBlank
    String as;
  }

  public enum TaskState {
    CRITICAL,
    WARNING,
    INFO,
    OK
  }

  public enum Comparator  {
    EQUAL_TO,
    NOT_EQUAL_TO,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN,
    LESS_THAN_OR_EQUAL_TO,
    REGEX_MATCH,
    NOT_REGEX_MATCH;

    static private final HashMap<String, Comparator> convertString = new HashMap<>();
    static {
      convertString.put("==", EQUAL_TO);
      convertString.put("!=", NOT_EQUAL_TO);
      convertString.put(">", GREATER_THAN);
      convertString.put("<", LESS_THAN);
      convertString.put(">=", GREATER_THAN_OR_EQUAL_TO);
      convertString.put("<=", LESS_THAN_OR_EQUAL_TO);
      convertString.put("=~", REGEX_MATCH);
      convertString.put("!~", NOT_REGEX_MATCH);
    }
    static public boolean valid(String c) {
        return (convertString.get(c) != null);
    }
  }

  @Data
  public static class StateExpression {
    Expression expression;
    TaskState state;
    String message;
  }

  @JsonTypeInfo(use = Id.NAME, property = "type")
  @JsonSubTypes({
      @Type(name = "logical", value = LogicalExpression.class),
      @Type(name = "comparison", value = ComparisonExpression.class)})
  public static abstract class Expression {
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class LogicalExpression extends Expression {
    Operator operator;
    List<Expression> expressions;

    public enum Operator {
      AND, OR
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ComparisonExpression extends Expression {
    @ExpressionValidator.ComparatorValidation()
    String comparator;
    String metricName;
    Object comparisonValue;

    /**
     * A method used within tests to help podam know how to populate the threshold field.
     * Otherwise it does not know what to insert for an 'Object'.
     * @param value A random string value to be used as the threshold.
     */
    public void podamHelper(String value) {
      this.comparisonValue = value;
    }
  }
}
