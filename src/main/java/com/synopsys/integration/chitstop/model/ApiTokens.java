/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ApiTokens {
    public static final List<String> VM_KEYS = List.of("bdstarlabs", "relcandi", "hub01", "hub02", "hub03", "hub04", "swarm01", "auto03");

    public static final String DEFAULT_USERNAME = "sysadmin";

    private Map<String, Map<String, ApiToken>> apiTokenStore = new HashMap<>();

    public ApiTokens() {
        for (String vmKey : VM_KEYS) {
            apiTokenStore.put(vmKey, new HashMap<>());
        }
    }

    public void addToken(ApiToken apiToken) {
        apiTokenStore.get(apiToken.getVmKey()).put(apiToken.getName(), apiToken);
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
        Collection<ApiToken> apiTokens = apiTokenStore.get(lookupKey).values();
        return apiTokens
                   .stream()
                   .filter(apiToken -> username.equals(apiToken.getUsername()))
                   .filter(apiToken -> ApiTokenScope.READ_AND_WRITE.equals(apiToken.getScope()))
                   .findFirst()
                   .orElse(null);
    }

    public ApiToken findByVMAndTokenName(String vm, String tokenName) {
        String lookupKey = ApiToken.parseVmKey(vm);
        return apiTokenStore.get(lookupKey).get(tokenName);
    }

}
