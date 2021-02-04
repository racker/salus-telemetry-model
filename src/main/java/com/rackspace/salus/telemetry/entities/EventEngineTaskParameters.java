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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonValue;
import com.rackspace.salus.telemetry.model.ValidLabelKeys;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
// TODO validate zoneLabel is set when zoneQuorumCount > 1
public class EventEngineTaskParameters {

  @NotBlank
  String metricGroup;

  List<StateExpression> stateExpressions = new ArrayList<>();

  @ValidLabelKeys
  Map<String, String> labelSelector;

  /**
   * These labels are used in addition to the label selectors to identify distinct metric-streams
   * to evaluate.
   */
  @JsonInclude(Include.NON_EMPTY)
  List<String> groupBy;

  String messageTemplate;

  public enum TaskState {
    CRITICAL,
    WARNING,
    OK
  }

  /**
   * Declares an expected consecutive count for each task state.
   */
  @Min(1)
  int defaultConsecutiveCount = 1;

  /**
   * Events for local monitors would always set this to 1 (the default), but events for
   * remote monitors should set to the desired quorum count.
   */
  @Min(1)
  int zoneQuorumCount = 1;

  /**
   * When zoneQuorumCount is more than 1, then this field indicates the metric label that is
   * used to identifying the monitoring zone where the metric originated.
   */
  @JsonInclude(Include.NON_EMPTY)
  String zoneLabel;

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

    /**
     * When importing expressions from external sources the comparison might
     * have the literal, comparison value on the left. This method will flip
     * the meaning of the operator to normalize to the comparison value on the
     * right.
     * @return the commutative version of this operator
     */
    public Comparator flip() {
      switch (this) {
        case GREATER_THAN:
          return LESS_THAN;
        case GREATER_THAN_OR_EQUAL_TO:
          return LESS_THAN_OR_EQUAL_TO;
        case LESS_THAN:
          return GREATER_THAN;
        case LESS_THAN_OR_EQUAL_TO:
          return GREATER_THAN_OR_EQUAL_TO;
        default:
          return this;
      }
    }

    @JsonValue
    public String getFriendlyName() {
      return this.friendlyName;
    }
  }

  @Data
  public static class StateExpression {
    @NotNull @Valid
    Expression expression;
    @NotNull
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
    @NotNull
    Operator operator;
    @NotEmpty
    List<@Valid Expression> expressions;

    public enum Operator {
      AND, OR
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ComparisonExpression extends Expression {
    @NotNull
    Comparator comparator;
    /**
     * Can be the string name of a metric or one of the {@link Function} types.
     */
    @JsonTypeInfo(use = Id.NAME, property = "type")
    @JsonSubTypes(
        {
            @Type(name = "rate", value=RateFunction.class),
            @Type(name = "percentage", value=PercentageFunction.class),
            @Type(name = "previous", value=PreviousFunction.class)
        }
    )
    @Valid
    Object input;
    /**
     * Can be a string or number
     */
    @NotNull
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

  public static abstract class Function {
  }

  @EqualsAndHashCode(callSuper = false)
  @Data
  public static class RateFunction extends Function {
    @NotEmpty
    String of;
  }

  @EqualsAndHashCode(callSuper = false)
  @Data
  public static class PercentageFunction extends Function {
    @NotEmpty
    String part;
    @NotEmpty
    String whole;
  }

  @EqualsAndHashCode(callSuper = false)
  @Data
  public static class PreviousFunction extends Function {
    @NotEmpty
    String of;
  }
}
