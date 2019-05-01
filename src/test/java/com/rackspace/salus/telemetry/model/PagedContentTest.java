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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

    assertThat(converted.getContent(), hasSize(5));
    assertThat(converted.getContent(), contains("1", "3", "5", "7", "9"));
    assertThat(converted.isFirst(), equalTo(true));
    assertThat(converted.isLast(), equalTo(false));
    assertThat(converted.getNumber(), equalTo(3));
    assertThat(converted.getTotalElements(), equalTo(300L));
    assertThat(converted.getTotalPages(), equalTo(60));
  }

  @Test
  public void testFromPage() {

    final List<Integer> content = Arrays.asList(1, 3, 5, 7, 9);

    final PageImpl<Integer> page = new PageImpl<>(content, PageRequest.of(3, 5), 20);

    final PagedContent<Integer> result = PagedContent.fromPage(page);

    assertThat(result, notNullValue());
    assertThat(result.getContent(), hasSize(5));
    assertThat(result.getContent(), contains(1,3,5,7,9));
    assertThat(result.isFirst(), equalTo(false));
    assertThat(result.isLast(), equalTo(true));
    assertThat(result.getNumber(), equalTo(3));
    assertThat(result.getTotalElements(), equalTo(20L));
    assertThat(result.getTotalPages(), equalTo(4));
  }
}