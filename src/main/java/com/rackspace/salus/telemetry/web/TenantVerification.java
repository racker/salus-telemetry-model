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

package com.rackspace.salus.telemetry.web;

import com.rackspace.salus.telemetry.model.NotFoundException;
import com.rackspace.salus.telemetry.repositories.TenantMetadataRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TenantVerification extends HandlerInterceptorAdapter {

  public static final String ERROR_MSG = "Tenant must be created before any operations can be performed with it";

  public static final String HEADER_TENANT = "X-Tenant-Id";
  TenantMetadataRepository tenantMetadataRepository;

  public TenantVerification(TenantMetadataRepository repository) {
    super();
    this.tenantMetadataRepository = repository;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {
    String tenantId = request.getHeader(HEADER_TENANT);
    if (tenantId == null) {
      // cross-service requests will not contain the header.
      // neither should any request to the admin api.
      // only requests proxies from the public api will.
      return true;
    } else if (tenantMetadataRepository.existsByTenantId(tenantId)) {
      return true;
    } else {
      throw new NotFoundException(ERROR_MSG);
    }
  }
}
