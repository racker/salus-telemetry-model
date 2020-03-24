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
 *
 */

package db.migration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

@Component
public class V4_2__set_translator_order extends BaseJavaMigration {

  @Override
  public void migrate(Context context) throws Exception {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
    jdbcTemplate.execute("UPDATE monitor_translation_operators SET priority_order = 0");

    List<MigrationTranslator> translators = jdbcTemplate
        .query("SELECT * FROM monitor_translation_operators WHERE (monitor_type = 'apache' OR monitor_type = 'http')", new MigrationTranslatorMapper());


    for(MigrationTranslator translator : translators) {
      jdbcTemplate.execute(String.format("UPDATE monitor_translation_operators SET priority_order = %d WHERE id = '%s'", translator.priority_order, translator.id));
    }
  }


  private class MigrationTranslator {
    UUID id;
    int priority_order;
  }

  private class MigrationTranslatorMapper implements RowMapper<MigrationTranslator> {

    @SneakyThrows
    @Override
    public MigrationTranslator mapRow(ResultSet resultSet, int i) throws SQLException {
      MigrationTranslator value = new MigrationTranslator();
      value.id = UUID.fromString(resultSet.getString("id"));
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.readTree(resultSet.getBytes("translator_spec"));
      JsonNode type = node.get("type");
      if(type.asText().equals("renameFieldKey")) {
        value.priority_order = 2;
      }else if(type.asText().equals("goDuration")) {
        value.priority_order = 1;
      }else {
        value.priority_order = 0;
      }
      return value;
    }
  }
}