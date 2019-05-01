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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * This data type provides a JSON ObjectMapper deserialization compatible target for content
 * serialized from {@link org.springframework.data.domain.Page}. As a result, it avoids the
 * default web response rendering of {@link Page} where the {@link Page#getPageable()} renders
 * with redundant fields from the top-level page.
 *
 * @param <T> the element type of the content
 */
@Data
public class PagedContent<T> {

  List<T> content;

  int number;
  int totalPages;
  long totalElements;
  boolean last;
  boolean first;

  /**
   * Similar to {@link Page#map(Function)}, this method allows for creating a new page that has
   * its content transformed via the given converter.
   * @param converter a function that takes the old element type and converts into the new element type
   * @param <U> the resulting content element data type
   * @return a new page with its content transformed, but all other fields left as before
   */
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

  /**
   * This utility method simplifies creation of {@link PagedContent} that contain only a single element.
   * @param content the single object to use as the content of the page
   * @param <T> the data type of the content element
   * @return a {@link PagedContent} populated with the single element and indicating that there is
   * only a single page that is the first and last.
   */
  public static <T> PagedContent<T> ofSingleton(T content) {
    return new PagedContent<T>()
        .setContent(Collections.singletonList(content))
        .setNumber(0)
        .setTotalPages(1)
        .setTotalElements(1)
        .setLast(true)
        .setFirst(true);
  }

  /**
   * This conversion is intended for final web responses converting from the {@link Page} provided
   * by a datastore like a {@link org.springframework.stereotype.Repository}.
   * @param page the page provided by a datastore
   * @param <T> the data type of the content elements
   * @return a {@link PagedContent} with similar fields set identical to the given page
   */
  public static <T> PagedContent<T> fromPage(Page<T> page) {
    return new PagedContent<T>()
        .setContent(page.getContent())
        .setNumber(page.getNumber())
        .setTotalPages(page.getTotalPages())
        .setTotalElements(page.getTotalElements())
        .setLast(page.isLast())
        .setFirst(page.isFirst());
  }
}
