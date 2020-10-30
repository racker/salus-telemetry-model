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

alter table agent_install_label_selectors add foreign key (agent_install_id) references agent_installs (id);
alter table agent_release_labels add foreign key (agent_release_id) references agent_releases (id);
alter table bound_agent_installs add foreign key (agent_install_id) references agent_installs (id);
alter table bound_monitors add foreign key (monitor_id) references monitors (id);
alter table metadata_policies add foreign key (id) references policies (id);
alter table monitor_label_selectors add foreign key (monitor_id) references monitors (id);
alter table monitor_metadata_fields add foreign key (monitor_id) references monitors (id);
alter table monitor_metadata_policies add foreign key (id) references metadata_policies (id);
alter table monitor_policies add foreign key (id) references policies (id);
alter table plugin_metadata_fields add foreign key (monitor_id) references monitors (id);
alter table resource_labels add foreign key (id) references resources (id);