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

public enum TargetClassName {
  LocalPlugin,
  RemotePlugin,
  Monitor;

  public static TargetClassName getTargetClassName(Object object) {
    TargetClassName className;
    try {
      className = TargetClassName.valueOf(object.getClass().getSimpleName());
    } catch (IllegalArgumentException e) {
      try {
        className = TargetClassName.valueOf(object.getClass().getSuperclass().getSimpleName());
      } catch (IllegalArgumentException e1) {
        throw new IllegalStateException(
            String.format("%s and %s are not a valid TargetClassNames",
                object.getClass().getSimpleName(),
                object.getClass().getSuperclass().getSimpleName()));
      }
    }
    return className;
  }
}
