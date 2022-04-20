/*
 * chitstop
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.vms;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfig;
import com.synopsys.integration.chitstop.rest.model.ApiToken;
import com.synopsys.integration.chitstop.rest.model.VmKey;
import com.synopsys.integration.chitstop.service.ApiTokens;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

@Service
public class BlackDuckVmService {
    private final ApiTokens apiTokens;

    @Autowired
    public BlackDuckVmService(ApiTokens apiTokens) {
        this.apiTokens = apiTokens;
    }

    public List<BlackDuckServerConfig> findAllBlackDuckServerConfigs() throws IntegrationException {
        Map<VmKey, ApiToken> vmTokens = apiTokens.findTokensByUsernameAndScope(ApiTokens.DEFAULT_USERNAME, ApiTokens.DEFAULT_SCOPE);

        List<BlackDuckServerConfig> blackDuckServerConfigs = new LinkedList<>();
        for (Map.Entry<VmKey, ApiToken> entry : vmTokens.entrySet()) {
            HttpUrl blackDuckUrl = entry.getKey().https();
            String username = ApiTokens.DEFAULT_USERNAME;
            String token = entry.getValue().getToken();

            BlackDuckServerConfig blackDuckServerConfig =
                BlackDuckServerConfig
                    .newBuilder()
                    .setUrl(blackDuckUrl)
                    .setUsername(username)
                    .setApiToken(token)
                    .build();

            blackDuckServerConfigs.add(blackDuckServerConfig);
        }

        return blackDuckServerConfigs;
    }

}
