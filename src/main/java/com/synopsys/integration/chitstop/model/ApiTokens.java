/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.model;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.synopsys.integration.chitstop.storage.ApiTokenStorage;

@Component
public class ApiTokens {
    public static final BiFunction<String, String, Predicate<ApiToken>> FIND_BY_USERNAME = (lookupKey, username) -> (Predicate<ApiToken>) apiToken -> lookupKey.equals(apiToken.getVmKey()) && username.equals(apiToken.getUsername())
                                                                                                                                                          && ApiTokenScope.READ_AND_WRITE.equals(apiToken.getScope());
    public static final BiFunction<String, String, Predicate<ApiToken>> FIND_BY_NAME = (lookupKey, name) -> (Predicate<ApiToken>) apiToken -> lookupKey.equals(apiToken.getVmKey()) && name.equals(apiToken.getName())
                                                                                                                                                  && ApiTokenScope.READ_AND_WRITE.equals(apiToken.getScope());

    public static final String DEFAULT_USERNAME = "sysadmin";

    private final ApiTokenStorage apiTokenStorage;

    @Autowired
    public ApiTokens(ApiTokenStorage apiTokenStorage) {
        this.apiTokenStorage = apiTokenStorage;
    }

    public void addToken(ApiToken apiToken) {
        apiTokenStorage.addApiToken(apiToken);
    }

    public ApiToken retrieve(String vm, String username, String name) {
        if (StringUtils.isNotBlank(name)) {
            return findByVMAndTokenName(vm, name);
        } else if (StringUtils.isNotBlank(username)) {
            return findByVMAndUsername(vm, username);
        } else {
            return findByVM(vm);
        }
    }

    public String retrievePure(String vm, String username, String name) {
        return Optional.ofNullable(retrieve(vm, username, name))
                   .map(ApiToken::getToken)
                   .orElse("");
    }

    /**
     * Will use only what lies between https and .dc1.lan
     * ex:
     * https://name.dc1.lan
     * name
     */
    public ApiToken findByVM(String vm) {
        return findByVMAndUsername(vm, DEFAULT_USERNAME);
    }

    public ApiToken findByVMAndUsername(String vm, String username) {
        String lookupKey = ApiToken.parseVmKey(vm);
        return apiTokenStorage.retrieveApiTokens()
                   .stream()
                   .filter(apiToken -> lookupKey.equals(apiToken.getVmKey()))
                   .filter(apiToken -> username.equals(apiToken.getUsername()))
                   .filter(apiToken -> ApiTokenScope.READ_AND_WRITE.equals(apiToken.getScope()))
                   .findFirst()
                   .orElse(null);
    }

    public ApiToken findByVMAndTokenName(String vm, String tokenName) {
        String lookupKey = ApiToken.parseVmKey(vm);
        return apiTokenStorage.retrieveApiTokens()
                   .stream()
                   .filter(apiToken -> lookupKey.equals(apiToken.getVmKey()))
                   .filter(apiToken -> tokenName.equals(apiToken.getName()))
                   .filter(apiToken -> ApiTokenScope.READ_AND_WRITE.equals(apiToken.getScope()))
                   .findFirst()
                   .orElse(null);
    }

}
