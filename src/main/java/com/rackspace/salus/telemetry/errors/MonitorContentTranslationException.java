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

package com.rackspace.salus.telemetry.errors;

import com.rackspace.salus.telemetry.entities.BoundMonitor;
import com.rackspace.salus.telemetry.translators.MonitorTranslator;

/**
 * Indicates that an issue was encountered with either a {@link MonitorTranslator} or the
 * {@link BoundMonitor}'s rendered content that prevented translation.
 */
public class MonitorContentTranslationException extends Exception {

  public MonitorContentTranslationException(String message) {
    super(message);
  }

  public MonitorContentTranslationException(String message, Throwable cause) {
    super(message, cause);
  }
}
