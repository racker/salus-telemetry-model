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

CREATE TABLE envoy_tokens
(
    id                VARCHAR(255) PRIMARY KEY,
    token             VARCHAR(50)  NOT NULL,
    tenant_id         VARCHAR(255) NOT NULL,
    description       VARCHAR(255),
    created_timestamp DATETIME     NOT NULL,
    updated_timestamp DATETIME,
    last_used         DATETIME
) engine = InnoDB;

CREATE INDEX envoy_tokens_by_token
    ON envoy_tokens (token);

CREATE INDEX envoy_tokens_by_tenant_token
    ON envoy_tokens (tenant_id, token)