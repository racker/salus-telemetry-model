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

package com.rackspace.salus.telemetry.translators;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rackspace.salus.telemetry.errors.MonitorContentTranslationException;

/**
 * This abstract class is a base class for implementations of monitor content translators. Each
 * subclass should declare validated properties to specify the translation and the implementation of
 * {@link #translate(ObjectNode)}.
 * <p>
 * As a new translator sub-class is introduced and entry must be added to the {@link JsonSubTypes}
 * where the type name is assigned for each.
 * </p>
 * <p>
 * <em>Implementation Note</em>: mixing a data type with implementation details is an anti-pattern,
 * but the
 * co-location of both aspects ensures a robust, maintainable strategy as more translator types are
 * implemented.
 * </p>
 */
@JsonTypeInfo(use = Id.NAME, property = MonitorTranslator.TYPE_PROPERTY)
@JsonSubTypes({
    @Type(name = "durationInSeconds", value = DurationInSecondsTranslator.class),
    @Type(name = "goDuration", value = GoDurationTranslator.class),
    @Type(name = "joinHostPort", value = JoinHostPortTranslator.class),
    @Type(name = "renameType", value = RenameTypeTranslator.class),
    @Type(name = "renameFieldKey", value = RenameFieldKeyTranslator.class),
    @Type(name = "replaceStringFieldValue", value = ReplaceStringFieldValueTranslator.class),
    @Type(name = "replaceIntFieldValue", value = ReplaceIntFieldValueTranslator.class),
    @Type(name = "scalarToArray", value = ScalarToArrayTranslator.class)
})
public abstract class MonitorTranslator {

  public static final String TYPE_PROPERTY = "type";

  /**
   * Translate the given monitor content tree for the monitor of the given type.
   *
   * @param contentTree can be manipulated in place, if this translator finds it is applicable
   */
  public abstract void translate(ObjectNode contentTree) throws MonitorContentTranslationException;

  public abstract String info();

}
