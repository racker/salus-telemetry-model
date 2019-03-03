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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class Label {
    String name;
    String value;

    public static Map<String, String> convertToMap(List<Label> labels) {
        if (labels == null) {
            return null;
        }
        return labels.stream()
            .collect(Collectors.toMap(Label::getName, Label::getValue));
    }

    public static List<Label> convertToLabelList(Map<String, String> labels) {
        if (labels == null) {
            return null;
        }
        return labels.entrySet().stream()
            .map(entry -> new Label().setName(entry.getKey()).setValue(entry.getValue()))
            .collect(Collectors.toList());
    }
}
