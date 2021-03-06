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

CREATE INDEX by_zone_and_poller ON bound_monitors (zone_name, poller_resource_id);
CREATE INDEX by_tenant_zone_and_poller ON bound_monitors (tenant_id, zone_name, poller_resource_id);
DROP INDEX by_poller_resource_id ON bound_monitors;
DROP INDEX by_tenant_poller_resource_id ON bound_monitors;