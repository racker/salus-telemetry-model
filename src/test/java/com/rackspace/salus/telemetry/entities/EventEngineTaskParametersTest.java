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

import static org.assertj.core.api.Assertions.assertThat;

import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.Comparator;
import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.ComparisonExpression;
import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.PercentageFunction;
import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.PreviousFunction;
import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.RateFunction;
import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.StateExpression;
import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.TaskState;
import com.rackspace.salus.test.JsonTestUtils;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

@SuppressWarnings("AssertBetweenInconvertibleTypes") // IntelliJ is confused about isEqualTo
@RunWith(SpringRunner.class)
@JsonTest
public class EventEngineTaskParametersTest {

  /**
   * Stub config since we're testing within a library module
   */
  @SpringBootConfiguration
  public static class TestConfig {

  }

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // IntelliJ can't find it
  @Autowired
  JacksonTester<EventEngineTaskParameters> json;

  @Test
  public void testComparisionExpression_inputNamedMetric() throws IOException {
    final String content = JsonTestUtils
        .readContent("/EventEngineTaskParametersTest/comparison_named_metric_input.json");
    assertThat(json.parse(content))
        .isEqualTo(
            new EventEngineTaskParameters()
                .setStateExpressions(List.of(
                    new StateExpression()
                        .setState(TaskState.CRITICAL)
                        .setExpression(
                            new ComparisonExpression()
                                .setComparator(Comparator.GREATER_THAN)
                                .setInput("errors")
                                .setComparisonValue(0)
                        )
                ))
        );
  }

  @Test
  public void testComparisionExpression_inputRate() throws IOException {
    final String content = JsonTestUtils
        .readContent("/EventEngineTaskParametersTest/comparison_rate_input.json");
    assertThat(json.parse(content))
        .isEqualTo(
            new EventEngineTaskParameters()
                .setStateExpressions(List.of(
                    new StateExpression()
                        .setState(TaskState.CRITICAL)
                        .setExpression(
                            new ComparisonExpression()
                                .setComparator(Comparator.GREATER_THAN)
                                .setInput(
                                    new RateFunction()
                                        .setOf("errors")
                                )
                                .setComparisonValue(0)
                        )
                ))
        );
  }

  @Test
  public void testComparisionExpression_inputPercentage() throws IOException {
    final String content = JsonTestUtils
        .readContent("/EventEngineTaskParametersTest/comparison_percentage_input.json");
    assertThat(json.parse(content))
        .isEqualTo(
            new EventEngineTaskParameters()
                .setStateExpressions(List.of(
                    new StateExpression()
                        .setState(TaskState.CRITICAL)
                        .setExpression(
                            new ComparisonExpression()
                                .setComparator(Comparator.GREATER_THAN)
                                .setInput(
                                    new PercentageFunction()
                                        .setPart("errors")
                                        .setWhole("total")
                                )
                                .setComparisonValue(0)
                        )
                ))
        );
  }

  @Test
  public void testComparisionExpression_inputPrevious() throws IOException {
    final String content = JsonTestUtils
        .readContent("/EventEngineTaskParametersTest/comparison_previous_input.json");
    assertThat(json.parse(content))
        .isEqualTo(
            new EventEngineTaskParameters()
                .setStateExpressions(List.of(
                    new StateExpression()
                        .setState(TaskState.CRITICAL)
                        .setExpression(
                            new ComparisonExpression()
                                .setComparator(Comparator.GREATER_THAN)
                                .setInput(
                                    new PreviousFunction()
                                        .setOf("errors")
                                )
                                .setComparisonValue(0)
                        )
                ))
        );
  }

  @Test
  public void testComparisionExpression_stringComparisonValue() throws IOException {
    final String content = JsonTestUtils
        .readContent("/EventEngineTaskParametersTest/comparison_string_comparision_value.json");
    assertThat(json.parse(content))
        .isEqualTo(
            new EventEngineTaskParameters()
                .setStateExpressions(List.of(
                    new StateExpression()
                        .setState(TaskState.OK)
                        .setExpression(
                            new ComparisonExpression()
                                .setComparator(Comparator.REGEX_MATCH)
                                .setInput("message")
                                .setComparisonValue("success")
                        )
                ))
        );
  }
}