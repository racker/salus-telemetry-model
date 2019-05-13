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

import java.util.Map;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that the annotated map contains keys that contain only letters, digits, and underscore.
 * A null field is also considered valid with the assumption
 * that {@link javax.validation.constraints.NotNull} will be combined on the field to further
 * constrain non-null-ness.
 */
public class ValidLabelKeysValidator implements ConstraintValidator<ValidLabelKeys, Map<String,String>> {
   private static final Pattern VALID_KEY = Pattern.compile("[A-Za-z0-9_]+");

   public void initialize(ValidLabelKeys constraint) {
   }

   public boolean isValid(Map<String,String> obj, ConstraintValidatorContext context) {
      if (obj == null) {
         return true;
      }
      return obj.keySet().stream()
          .allMatch(key -> VALID_KEY.matcher(key).matches());
   }

}
