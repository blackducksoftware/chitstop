/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.model;

import com.synopsys.integration.util.Stringable;

public class ApiToken extends Stringable {
    private final String vmKey;
    private final String username;

    private String token;
    private String tokenName;
    private String description;
    private ApiTokenScope scope;

    public static String parseVmKey(String vmKey) {
        return vmKey
                   .replace("https://", "")
                   .replace("int-", "")
                   .replace(".dc1.lan", "");
    }

    public ApiToken(String vmKey, String username) {
        this.vmKey = vmKey;
        this.username = username;
    }

    public ApiToken(String token, String vmKey, String tokenName, String description, ApiTokenScope scope, String username) {
        this.token = token;
        this.vmKey = parseVmKey(vmKey);
        this.tokenName = tokenName;
        this.description = description;
        this.scope = scope;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVmKey() {
        return vmKey;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApiTokenScope getScope() {
        return scope;
    }

    public void setScope(ApiTokenScope scope) {
        this.scope = scope;
    }

    public String getUsername() {
        return username;
    }

}
