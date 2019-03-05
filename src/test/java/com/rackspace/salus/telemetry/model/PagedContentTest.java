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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Test;

public class PagedContentTest {

  @Test
  public void testMapping() {
    final PagedContent<Integer> original = new PagedContent<Integer>()
        .setContent(Arrays.asList(1, 3, 5, 7, 9))
        .setFirst(true)
        .setLast(false)
        .setNumber(3)
        .setTotalElements(300)
        .setTotalPages(60);

    final PagedContent<String> converted = original.map(integer -> Integer.toString(integer));

    assertEquals(Arrays.asList("1", "3", "5", "7", "9"), converted.getContent());
    assertTrue(converted.isFirst());
    assertFalse(converted.isLast());
    assertEquals(3, converted.getNumber());
    assertEquals(300, converted.getTotalElements());
    assertEquals(60, converted.getTotalPages());

  }
}