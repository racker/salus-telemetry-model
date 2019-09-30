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

/**
 * This package contains monitor content translators -- not to be confused with i18n/language
 * translation support.
 * The top-level, abstract data type is {@link com.rackspace.salus.telemetry.translators.MonitorTranslator}
 * and other concrete classes in this package are specific translators that declare the structure
 * and implementation of each specific translator.
 */
package com.rackspace.salus.telemetry.translators;