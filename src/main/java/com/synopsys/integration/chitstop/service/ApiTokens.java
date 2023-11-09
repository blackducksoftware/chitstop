/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synopsys.integration.chitstop.rest.model.ApiToken;
import com.synopsys.integration.chitstop.rest.model.ApiTokenScope;
import com.synopsys.integration.chitstop.rest.model.TokenDetails;
import com.synopsys.integration.chitstop.rest.model.VmKey;
import com.synopsys.integration.chitstop.rest.model.VmTokensStatus;

@Service
public class ApiTokens {
    public static final String DEFAULT_USERNAME = "sysadmin";
    public static final ApiTokenScope DEFAULT_SCOPE = ApiTokenScope.READ_AND_WRITE;

    private final ApiTokenStorage apiTokenStorage;

    @Autowired
    public ApiTokens(ApiTokenStorage apiTokenStorage) {
        this.apiTokenStorage = apiTokenStorage;
    }

    public Set<VmKey> findAllVmKeys() {
        return apiTokenStorage
                   .retrieveApiTokens()
                   .stream()
                   .map(apiToken -> apiToken.getTokenDetails().getVmKey())
                   .collect(Collectors.toSet());
    }

    public Map<VmKey, ApiToken> findTokensByUsernameAndScope(String username, ApiTokenScope scope) {
        return
            apiTokenStorage
                .retrieveApiTokens()
                .stream()
                .filter(apiToken -> apiToken.getTokenDetails().getUsername().equals(username))
                .filter(apiToken -> apiToken.getTokenDetails().getScope().equals(scope))
                .collect(Collectors.toMap(apiToken -> apiToken.getTokenDetails().getVmKey(), Function.identity(), (existing, replacement) -> existing));
    }

    public String tokensStatusReport() {
        Set<VmKey> allVmKeys = findAllVmKeys();
        Map<VmKey, ApiToken> sysadminTokens = findTokensByUsernameAndScope(DEFAULT_USERNAME, DEFAULT_SCOPE);

        List<VmTokensStatus> tokensStatuses = new LinkedList<>();
        for (VmKey vmKey : allVmKeys) {
            if (sysadminTokens.containsKey(vmKey)) {
                tokensStatuses.add(VmTokensStatus.okay(vmKey));
            } else {
                tokensStatuses.add(VmTokensStatus.missing(vmKey));
            }
        }

        Collections.sort(tokensStatuses, Comparator.comparing(status -> status.getVmKey().getValue()));

        StringBuilder stringBuilder = new StringBuilder();
        for (VmTokensStatus tokenStatus : tokensStatuses) {
            stringBuilder.append(String.format("%s: %s\n", tokenStatus.getVmKey().getValue(), tokenStatus.getStatus()));
        }
        return stringBuilder.toString();
    }

    public void storeToken(ApiToken apiToken) {
        apiTokenStorage.storeApiToken(apiToken);
    }

    public List<ApiToken> findAll() {
        return findAll(null);
    }

    public List<ApiToken> findAll(VmKey vmKey) {
        if (null == vmKey) {
            return apiTokenStorage.retrieveApiTokens();
        }

        return apiTokenStorage
                   .retrieveApiTokens()
                   .stream()
                   .filter((apiToken) -> apiToken.getTokenDetails().getVmKey().equals(vmKey))
                   .collect(Collectors.toList());
    }

    public ApiToken find(VmKey vmKey, String username) {
        if (StringUtils.isBlank(username)) {
            return findDefaultToken(vmKey);
        } else {
            return findByVMAndUsername(vmKey, username);
        }
    }

    public String findPure(VmKey vmKey, String username) {
        return Optional.ofNullable(find(vmKey, username))
                   .map(ApiToken::getToken)
                   .orElse("");
    }

    public ApiToken findDefaultToken(VmKey vmKey) {
        return findByVMAndUsername(vmKey, DEFAULT_USERNAME);
    }

    public ApiToken findByVMAndUsername(VmKey vmKey, String username) {
        List<ApiToken> found = apiTokenStorage.retrieveApiTokens()
                                   .stream()
                                   .filter((token) -> token.getTokenDetails().getVmKey().equals(vmKey))
                                   .filter((token) -> token.getTokenDetails().getUsername().equals(username))
                                   .collect(Collectors.toList());

        if (found.isEmpty()) {
            return null;
        }

        Collections.sort(found, ApiTokenStorage.BY_SCOPE);
        return found.get(0);
    }

    public ApiToken findByVMAndUsernameAndScope(VmKey vmKey, String username, ApiTokenScope scope) {
        TokenDetails lookingFor = new TokenDetails(vmKey, username, scope);
        return findByTokenDetails(lookingFor);
    }

    public ApiToken findByTokenDetails(TokenDetails lookingFor) {
        return apiTokenStorage.retrieveApiTokens()
                   .stream()
                   .filter((token) -> token.sameDetails(lookingFor))
                   .findFirst()
                   .orElse(null);
    }

    public void deleteVmTokens(VmKey vmKey) {
        List<ApiToken> vmTokens = findAll(vmKey);
        apiTokenStorage.removeApiTokens(vmTokens);
    }

    public void deleteVmToken(VmKey vmKey, String token) {
        findAll(vmKey)
            .stream()
            .filter((apiToken) -> apiToken.getToken().equals(token))
            .findFirst()
            .ifPresent(apiTokenStorage::removeApiToken);
    }

}
