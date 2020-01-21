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

package com.rackspace.salus.telemetry.model;

public enum MonitorType {
  // powered by telegraf
  apache,
  ping,
  http,
  net_response,
  ssl,
  cpu,
  disk,
  diskio,
  dns,
  mem,
  net,
  procstat,
  mysql,
  postgresql,
  redis,
  sqlserver,
  log,
  system,
  // powered by https://github.com/racker/salus-oracle-agent
  oracle_tablespace,
  oracle_dataguard,
  oracle_rman,
  // powered by https://github.com/racker/salus-packages-agent
  packages
}
