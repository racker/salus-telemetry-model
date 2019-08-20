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

package com.rackspace.salus.telemetry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import java.util.Map;
import lombok.Data;

/**
 * This is a simplified form of the metric structure used in Envoy-Ambassador protocol
 * and UMB, but avoids dependencies on either technology.
 */
@Data
@JsonInclude(Include.NON_NULL)
public class SimpleNameTagValueMetric {
  Date timestamp;
  String name;
  Map<String,String> tags;
  Map<String,Long> ivalues;
  Map<String,Double> fvalues;
  Map<String,String> svalues;
}
