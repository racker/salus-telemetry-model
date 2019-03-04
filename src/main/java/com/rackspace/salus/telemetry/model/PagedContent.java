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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * This data type provides a JSON ObjectMapper deserialization compatible target for content
 * serialized from {@link org.springframework.data.domain.Page}
 *
 * @param <T> the element type of the content
 */
@Data
public class PagedContent<T> {

  List<T> content;

  int number;
  int totalPages;
  int totalElements;
  boolean last;
  boolean first;

  public <U> PagedContent<U> map(Function<? super T, ? extends U> converter) {
    final PagedContent<U> converted = new PagedContent<U>()
        .setFirst(this.first)
        .setLast(this.last)
        .setNumber(this.number)
        .setTotalElements(this.totalElements)
        .setTotalPages(this.totalPages);

    converted.setContent(
        this.content.stream()
            .map(converter)
            .collect(Collectors.toList())
    );

    return converted;
  }
}
