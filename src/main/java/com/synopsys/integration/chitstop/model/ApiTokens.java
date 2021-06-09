/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.model;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.synopsys.integration.chitstop.storage.ApiTokenStorage;

@Component
public class ApiTokens {
    public static final String DEFAULT_USERNAME = "sysadmin";

    public static final Function<String, Predicate<ApiToken>> BY_COMMON = (vm) -> (apiToken) -> apiToken.getVmKey().equals(ApiToken.parseVmKey(vm)) && apiToken.getScope().equals(ApiTokenScope.READ_AND_WRITE);
    public static final Function<String, Predicate<ApiToken>> BY_TOKEN_NAME = (tokenName) -> (apiToken) -> apiToken.getTokenName().equals(tokenName);
    public static final Function<String, Predicate<ApiToken>> BY_USERNAME = (username) -> (apiToken) -> apiToken.getUsername().equals(username);

    private final ApiTokenStorage apiTokenStorage;

    @Autowired
    public ApiTokens(ApiTokenStorage apiTokenStorage) {
        this.apiTokenStorage = apiTokenStorage;
    }

    public void storeToken(ApiToken apiToken) {
        apiTokenStorage.storeApiToken(apiToken);
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
        return apiTokenStorage.retrieveApiTokens()
                   .stream()
                   .filter(BY_COMMON.apply(vm))
                   .filter(BY_USERNAME.apply(username))
                   .findFirst()
                   .orElse(null);
    }

    public ApiToken findByVMAndTokenName(String vm, String tokenName) {
        return apiTokenStorage.retrieveApiTokens()
                   .stream()
                   .filter(BY_COMMON.apply(vm))
                   .filter(BY_TOKEN_NAME.apply(tokenName))
                   .findFirst()
                   .orElse(null);
    }

}
