/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ApiTokens {
    public static final List<String> VM_KEYS = List.of("bdstarlabs", "relcandi", "hub01", "hub02", "hub03", "hub04", "swarm01", "auto03");

    public static final String DEFAULT_USERNAME = "sysadmin";

    private Map<String, Map<String, ApiToken>> apiTokenStore = new HashMap<>();
    private Map<String, Map<String, List<ApiToken>>> apiTokenUserMap = new HashMap<>();

    public ApiTokens() {
        for (String vmKey : VM_KEYS) {
            apiTokenStore.put(vmKey, new HashMap<>());
            apiTokenUserMap.put(vmKey, new HashMap<>());
        }
        addToken("bdstarlabs", new ApiToken("==", "sysadmin_read_and_write", "", ApiTokenScope.READ_AND_WRITE, "sysadmin"));
        addToken("bdstarlabs", new ApiToken("++==", "sysadmin_just_read", "", ApiTokenScope.READ, "sysadmin"));
    }

    private void addToken(String vmKey, ApiToken apiToken) {
        apiTokenStore.get(vmKey).put(apiToken.name, apiToken);
        apiTokenUserMap.get(vmKey).computeIfAbsent(apiToken.username, key -> new LinkedList<>()).add(apiToken);
    }

    /**
     * Will use only what lies between https and .dc1.lan
     * ex:
     * https://name.dc1.lan
     * name
     */
    public ApiToken findByVM(String vm) {
        String lookupKey = parseLookupKey(vm);
        return apiTokenUserMap.get(lookupKey).get(DEFAULT_USERNAME).get(0);
    }

    public ApiToken findByVMAndUsername(String vm, String username) {
        String lookupKey = parseLookupKey(vm);
        return apiTokenUserMap.get(lookupKey).get(username).get(0);
    }

    public ApiToken findByVMAndTokenName(String vm, String tokenName) {
        String lookupKey = parseLookupKey(vm);
        return apiTokenStore.get(lookupKey).get(tokenName);
    }

    public void loadTokens() {
        // TODO implement this instead of just using the constructor
    }

    private String parseLookupKey(String vm) {
        return vm
                   .replace("https://", "")
                   .replace("int-", "")
                   .replace(".dc1.lan", "");
    }

}
