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

create table state_changes
(
    id                   varchar(255) not null,
    contributing_events  varchar(255),
    created_timestamp    datetime,
    evaluation_timestamp datetime,
    message              varchar(255),
    monitor_id           binary(255),
    previous_state       varchar(255),
    resource_id          varchar(255),
    state                varchar(255),
    task_id              binary(255),
    tenant_id            varchar(255),
    updated_timestamp    datetime,
    primary key (id)
) engine = InnoDB