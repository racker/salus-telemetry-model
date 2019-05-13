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

import java.util.HashSet;
import java.util.Set;
import org.springframework.util.Assert;

public class LabelNamespaces {

  /**
   * Scopes the labels that were provided by an Envoy during attachment
   */
  public static final String AGENT = "agent";

  /**
   * Scopes the tags of the metrics ingested into the Event Engine that were fields originating
   * from top-level system identifiers, such as account and resource ID
   */
  public static final String EVENT_ENGINE_TAGS = "system";

  /**
   * Scopes the tags of the metrics ingested into the Event Engine that were supplied via the
   * system metadata.
   */
  public static final String MONITORING_SYSTEM_METADATA = "resource_metadata";

  private static final Set<String> ourNamespaces = new HashSet<>();
  private static final String DELIM = "_";

  static {
    ourNamespaces.add(AGENT);
    ourNamespaces.add(EVENT_ENGINE_TAGS);
    ourNamespaces.add(MONITORING_SYSTEM_METADATA);
  }

  /**
   * Applies the given namespace to the label name/key
   * @param namespace the namespace to apply
   * @param labelName the label's name
   * @return the qualified version of the label name
   */
  public static String applyNamespace(String namespace, String labelName) {
    Assert.hasText(namespace, "namespace is required");
    Assert.hasText(labelName, "labelName is required");
    return namespace + DELIM + labelName;
  }

  /**
   * Validates that the given user label does not attempt to use one of our namespaces.
   * @param label the user provided label
   * @return true if the user label is valid
   */
  public static boolean validateUserLabel(String label) {
    Assert.notNull(label, "label cannot be null");
    return ourNamespaces.stream()
        .noneMatch(namespace -> label.startsWith(namespace + DELIM));
  }

  public static boolean labelHasNamespace(String label, String namespace) {
    return label.startsWith(namespace + DELIM);
  }

  private LabelNamespaces() {}
}
