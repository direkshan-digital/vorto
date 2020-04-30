/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.spi.service;

import java.util.List;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServerGeneratorLookup implements IGeneratorLookup {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${vorto.serverUrl}")
  private String basePath;

  @Override
  public IVortoCodeGenerator lookupByKey(String key) {
    return new VortoCodeGeneratorProxy(key);
  }

  /**
   * 
   * Remote proxy that generates for the given service key via the Vorto Repository
   *
   */
  class VortoCodeGeneratorProxy implements IVortoCodeGenerator {

    private String key;

    public VortoCodeGeneratorProxy(String key) {
      this.key = key;
    }

    private String extractFileNameFromHeader(ResponseEntity<byte[]> entity) {
      List<String> values = entity.getHeaders().get("content-disposition");
      if (values.size() > 0) {
        int indexOfFileNameStart = values.get(0).indexOf("=");
        return values.get(0).substring(indexOfFileNameStart + 1);
      }
      return "generated.output";
    }

    @Override
    public String getServiceKey() {
      return key;
    }

    @Override
    public IGenerationResult generate(InformationModel model, InvocationContext context,
        IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
      restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
      ResponseEntity<byte[]> entity = restTemplate.getForEntity(
          basePath + "/rest/generation-router/{namespace}/{name}/{version}/{serviceKey}",
          byte[].class, model.getNamespace(), model.getName(), model.getVersion(), key);
      return new IGenerationResult() {

        @Override
        public String getMediatype() {
          return "application/zip";
        }

        @Override
        public String getFileName() {
          return extractFileNameFromHeader(entity);
        }

        @Override
        public byte[] getContent() {
          return entity.getBody();
        }
      };
    }

    @Override
    public GeneratorInfo getInfo() {
      return null;
    }

  }

}
