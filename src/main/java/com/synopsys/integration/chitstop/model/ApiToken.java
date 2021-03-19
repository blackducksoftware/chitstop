/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.model;

public class ApiToken {
    public final String token;

    public final String vmKey;
    public final String name;
    public final String description;
    public final ApiTokenScope scope;
    public final String username;

    public static String parseVmKey(String vmKey) {
        return vmKey
                   .replace("https://", "")
                   .replace("int-", "")
                   .replace(".dc1.lan", "");
    }

    public ApiToken(String token, String vmKey, String name, String description, ApiTokenScope scope, String username) {
        this.token = token;
        this.vmKey = parseVmKey(vmKey);
        this.name = name;
        this.description = description;
        this.scope = scope;
        this.username = username;
    }

}
