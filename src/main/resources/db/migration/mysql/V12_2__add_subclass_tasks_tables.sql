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

alter table event_engine_tasks drop column measurement;

create table generic_event_engine_tasks (
    measurement varchar(255),
    id varchar(255) not null,
    primary key (id),
    foreign key (id) references event_engine_tasks (id)
) engine=InnoDB;

create table salus_event_engine_tasks (
    monitor_type varchar(255) not null,
    monitor_selector_scope varchar(255) not null,
    id varchar(255) not null,
    primary key (id),
    foreign key (id) references event_engine_tasks (id)
) engine=InnoDB;
