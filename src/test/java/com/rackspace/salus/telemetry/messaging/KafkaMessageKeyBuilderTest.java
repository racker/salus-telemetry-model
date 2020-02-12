/*
 * Copyright 2020 Rackspace US, Inc.
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

package com.rackspace.salus.telemetry.messaging;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import lombok.Data;
import org.hamcrest.Matchers;
import org.junit.Test;

public class KafkaMessageKeyBuilderTest {

  @KafkaMessageKey(properties = "id")
  @Data
  static class OneProp {
    String id;
  }

  @Test
  public void oneProp() {
    final OneProp obj = new OneProp();
    obj.setId("testing-1");

    final String key = KafkaMessageKeyBuilder.buildMessageKey(obj);
    assertThat(key, equalTo("testing-1"));
  }

  @KafkaMessageKey(properties = {"id", "count", "enabled"})
  @Data
  static class MultipleProps {
    String id;
    int count;
    boolean enabled;
  }

  @Test
  public void multipleProps() {
    final MultipleProps obj = new MultipleProps();
    obj.setId("testing-2");
    obj.setCount(3);
    obj.setEnabled(true);

    final String key = KafkaMessageKeyBuilder.buildMessageKey(obj);
    assertThat(key, Matchers.equalTo("testing-2:3:true"));
  }

  @Test
  public void withNullProp() {
    final MultipleProps obj = new MultipleProps();
    // leave id unset, so it's null
    obj.setCount(5);
    obj.setEnabled(false);

    final String key = KafkaMessageKeyBuilder.buildMessageKey(obj);
    assertThat(key, Matchers.equalTo("null-id:5:false"));
  }

  @Data
  static class MissingAnnotation{
  }

  @Test(expected = IllegalArgumentException.class)
  public void missingAnnotation() {
    final MissingAnnotation obj = new MissingAnnotation();

    KafkaMessageKeyBuilder.buildMessageKey(obj);
  }

  @KafkaMessageKey(properties = {})
  static class EmptyPropsDeclared{}

  @Test(expected = IllegalArgumentException.class)
  public void emptyPropsDeclared() {
    final EmptyPropsDeclared obj = new EmptyPropsDeclared();

    KafkaMessageKeyBuilder.buildMessageKey(obj);
  }


  @KafkaMessageKey(properties = "id")
  static class InvalidProp {
    String notId;
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidProp() {
    final InvalidProp obj = new InvalidProp();

    KafkaMessageKeyBuilder.buildMessageKey(obj);
  }

  @Test
  public void buildMessageKeyFromParts_fromStringParts() {
    final String result = KafkaMessageKeyBuilder.buildMessageKeyFromParts("one", "two");

    assertThat(result, equalTo("one:two"));
  }

  @Test
  public void buildMessageKeyFromParts_fromOnePart() {
    final String result = KafkaMessageKeyBuilder.buildMessageKeyFromParts("one");

    assertThat(result, equalTo("one"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildMessageKeyFromParts_fromEmptyParts() {
    KafkaMessageKeyBuilder.buildMessageKeyFromParts();
  }

  @Test
  public void buildMessageKeyFromParts_fromMixedTypeParts() {
    final String result = KafkaMessageKeyBuilder.buildMessageKeyFromParts("one", 2, true);

    assertThat(result, equalTo("one:2:true"));
  }

  @Test
  public void buildMessageKeyFromParts_nullParts() {
    final String result = KafkaMessageKeyBuilder.buildMessageKeyFromParts("one", null);

    assertThat(result, equalTo("one:null"));
  }
}