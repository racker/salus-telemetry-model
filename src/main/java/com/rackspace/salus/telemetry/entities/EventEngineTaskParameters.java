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

package com.rackspace.salus.telemetry.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonValue;
import com.rackspace.salus.telemetry.model.MetricExpressionBase;
import com.rackspace.salus.telemetry.model.ValidLabelKeys;
import com.rackspace.salus.telemetry.validators.ValidCustomMetricList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

  @ValidCustomMetricList
  List<@Valid MetricExpressionBase> customMetrics;

  Integer windowLength;
  List<String> windowFields;

  boolean flappingDetection;

  @ValidLabelKeys
  Map<String, String> labelSelector;

  String messageTemplate;

  public enum TaskState {
    CRITICAL,
    WARNING,
    INFO
  }

  public enum Comparator  {
    EQUAL_TO("=="),
    NOT_EQUAL_TO("!="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<="),
    REGEX_MATCH("=~"),
    NOT_REGEX_MATCH("!~");

    private final String friendlyName;

    Comparator(String friendlyName) {
      this.friendlyName = friendlyName;
    }

    @JsonValue
    public String getFriendlyName() {
      return this.friendlyName;
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
    Comparator comparator;
    String valueName;
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
