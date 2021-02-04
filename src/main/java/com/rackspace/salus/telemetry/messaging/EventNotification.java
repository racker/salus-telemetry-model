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

package com.rackspace.salus.telemetry.messaging;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Data;

@Data
@KafkaMessageKey(properties = "id")
public class EventNotification {
  String id;
  String tenantId;
  Instant timestamp;
  String taskId;

  String state;
  String previousState;
  List<Observation> observations;
  String message;

  String metricGroup;
  Map<String, Object> metrics;

  List<Entry<String, String>> groupingLabels;
  Map<String,String> labels;

  @Data
  public static class Observation {
    String zone;
    String state;
  }
}
