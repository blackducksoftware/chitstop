/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.synopsys.integration.util.Stringable;

public class ApiToken extends Stringable {
    @NotNull
    private final TokenDetails tokenDetails;

    @NotBlank
    private final String token;

    public ApiToken(TokenDetails tokenDetails, String token) {
        this.tokenDetails = tokenDetails;
        this.token = token;
    }

    public boolean sameDetails(ApiToken that) {
        return sameDetails(that.tokenDetails);
    }

    public boolean sameDetails(TokenDetails that) {
        return tokenDetails.equals(that);
    }

    public TokenDetails getTokenDetails() {
        return tokenDetails;
    }

    public String getToken() {
        return token;
    }

}
