/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.synopsys.integration.util.Stringable;

public class TokenDetails extends Stringable {
    @NotNull
    private final VmKey vmKey;

    @NotBlank
    private final String username;

    @NotNull
    private final ApiTokenScope scope;

    public TokenDetails(VmKey vmKey, String username, ApiTokenScope scope) {
        this.vmKey = vmKey;
        this.username = username;
        this.scope = scope;
    }

    public VmKey getVmKey() {
        return vmKey;
    }

    public String getUsername() {
        return username;
    }

    public ApiTokenScope getScope() {
        return scope;
    }

}
