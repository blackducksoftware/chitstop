/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.model;

public class ApiToken {
    private String token;
    private String vmKey;
    private String name;
    private String description;
    private ApiTokenScope scope;
    private String username;

    public static String parseVmKey(String vmKey) {
        return vmKey
                   .replace("https://", "")
                   .replace("int-", "")
                   .replace(".dc1.lan", "");
    }

    public ApiToken() {
    }

    public ApiToken(String token, String vmKey, String name, String description, ApiTokenScope scope, String username) {
        this.token = token;
        this.vmKey = parseVmKey(vmKey);
        this.name = name;
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

    public void setVmKey(String vmKey) {
        this.vmKey = vmKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setUsername(String username) {
        this.username = username;
    }

}
