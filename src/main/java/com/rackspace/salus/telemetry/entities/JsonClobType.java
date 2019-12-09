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

package com.rackspace.salus.telemetry.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.annotations.Type;
import org.hibernate.engine.jdbc.internal.CharacterStreamImpl;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.sql.ClobTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;

/**
 * Provides a simple JSON to CLOB mapping for entity fields.
 * <p>
 *   Field to be encoded as JSON in a CLOB SQL column should specify "json" as the
 *   {@link Type#type}.
 * </p>
 * <p>
 * <em>NOTE</em> the hibernate-types library wasn't a viable solution since the SQL type
 * declared by the JSON string types is hardcoded as "OTHER".
 * </p>
 * <p>
 *   <a href="https://vladmihalcea.com/how-to-map-json-objects-using-generic-hibernate-types/">This article</a>
 *   was used as the basis for this implementation, but simplifies things by
 *   using <code>ClobTypeDescriptor.DEFAULT</code> rather than a custom type descriptor.
 * </p>
 */
public class JsonClobType
    extends AbstractSingleColumnStandardBasicType<Object>
    implements DynamicParameterizedType {

  public JsonClobType() {
    super(ClobTypeDescriptor.DEFAULT, new CustomTypeDescriptor());
  }

  @Override
  public String getName() {
    return "json";
  }

  @Override
  public void setParameterValues(Properties properties) {
    final ParameterType parameterType = (ParameterType) properties.get(PARAMETER_TYPE);
    if (parameterType != null) {
      ((CustomTypeDescriptor) getJavaTypeDescriptor())
          .setFieldClass(parameterType.getReturnedClass());
    }
  }

  private static class CustomTypeDescriptor extends
      AbstractTypeDescriptor<Object> {

    // It's a bummer we can't use Spring Boot's ObjectMapper, but Hibernate's type registry is
    // bootstrapped independently from the Spring app context
    final ObjectMapper objectMapper = new ObjectMapper();

    private Class<?> fieldClass;

    CustomTypeDescriptor() {
      super(Object.class);
    }

    @Override
    public Object fromString(String value) {
      try {
        return objectMapper.readValue(value, fieldClass);
      } catch (IOException e) {
        throw new IllegalArgumentException("Failed to deserialize JSON column", e);
      }
    }

    @Override
    public <X> Object wrap(X value, WrapperOptions wrapperOptions) {
      if (value instanceof String) {
        return fromString(((String) value));
      } else if (value instanceof Clob) {
        try {
          return objectMapper.readValue(((Clob) value).getCharacterStream(), fieldClass);
        } catch (IOException | SQLException e) {
          throw new IllegalArgumentException("Failed to deserialize JSON column", e);
        }
      }

      throw unknownWrap(value.getClass());
    }

    @Override
    public <X> X unwrap(Object obj, Class<X> aClass,
                        WrapperOptions wrapperOptions) {
      try {

        //noinspection unchecked
        return (X) new CharacterStreamImpl(objectMapper.writeValueAsString(obj));
      } catch (JsonProcessingException e) {
        throw new IllegalStateException("Failed to serialize object to JSON", e);
      }
    }

    public void setFieldClass(Class<?> fieldClass) {
      this.fieldClass = fieldClass;
    }
  }
}
