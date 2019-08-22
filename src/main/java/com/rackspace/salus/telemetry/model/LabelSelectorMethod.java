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

/**
 * This defines the operation to be used when performing label matching queries for
 * objects with a labelSelector field.
 *
 * For example, a method of AND on a Monitor means that all the labels on the Monitor must
 * be found on the Resource for the monitor to be applied.
 * A method of OR on a Monitor means that at least one of the labels on the Monitor must
 * be found on the Resource for the monitor to be applied.
 *
 */
public enum LabelSelectorMethod {
  AND,
  OR
}
