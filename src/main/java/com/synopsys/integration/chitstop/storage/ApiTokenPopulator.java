/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synopsys.integration.chitstop.model.ApiToken;
import com.synopsys.integration.chitstop.model.ApiTokens;

@Component
public class ApiTokenPopulator {
    private final ApiTokens apiTokens;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApiTokenPopulator(ApiTokens apiTokens, ObjectMapper objectMapper) {
        this.apiTokens = apiTokens;
        this.objectMapper = objectMapper;
    }

    public void populateApiTokens(Path apiTokensPath) throws IOException {
        String json = Files.readString(apiTokensPath, StandardCharsets.UTF_8);
        List<ApiToken> apiTokenList = objectMapper.readValue(json, new TypeReference<>() {});
        for (ApiToken apiToken : apiTokenList) {
            apiTokens.addToken(apiToken);
        }
    }

}
