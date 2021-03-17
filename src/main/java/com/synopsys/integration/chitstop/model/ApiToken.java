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

    public final String name;
    public final String description;
    public final ApiTokenScope scope;

    public final String username;

    public ApiToken(String token, String name, String description, ApiTokenScope scope, String username) {
        this.token = token;
        this.name = name;
        this.description = description;
        this.scope = scope;
        this.username = username;
    }

}
