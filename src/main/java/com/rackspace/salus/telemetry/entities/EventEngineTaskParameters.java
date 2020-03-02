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

import com.rackspace.salus.telemetry.model.ValidLabelKeys;
import com.rackspace.salus.telemetry.validators.EvalExpressionValidator.EvalExpressionValidation;
import com.rackspace.salus.telemetry.validators.ExpressionValidator;
import com.rackspace.salus.telemetry.validators.TaskParametersValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@TaskParametersValidator.AtLeastOneOf()
public class EventEngineTaskParameters {

  @Valid
  LevelExpression info;
  @Valid
  LevelExpression warning;
  @Valid
  LevelExpression critical;

  @Valid
  List<EvalExpression> evalExpressions;

  Integer windowLength;
  List<String> windowFields;

  boolean flappingDetection;

  @ValidLabelKeys
  Map<String, String> labelSelector;

  @Data
  public static class LevelExpression {

    @Valid
    Expression expression;
    Integer consecutiveCount = 1;
  }

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

  @Data
  public static class Expression {
    @NotEmpty
    String field;
    @NotNull
    Number threshold;
    @NotEmpty
    @ExpressionValidator.ComparatorValidation()
    String comparator;

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

    static private HashMap<String, Comparator> convertString = new HashMap<>();
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
}
