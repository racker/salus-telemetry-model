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

public enum PolicyScope {
  GLOBAL (0),
  ACCOUNT_TYPE (1),
  SLA (2),
  TENANT (3);

  private final int priority;
  PolicyScope(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return this.priority;
  }
}
