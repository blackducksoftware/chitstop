/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.rest.model;

import javax.validation.constraints.NotBlank;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;
import com.synopsys.integration.util.Stringable;

public class VmKey extends Stringable {
    @NotBlank
    private final String value;

    public VmKey(String vmKey) {
        value =
            vmKey
                .replace("https://", "")
                .replace("http://", "")
                .replace("us03-int-", "")
                .replace(".nprd.sig.synopsys.com", "");
    }

    public String getValue() {
        return value;
    }

    public HttpUrl https() throws IntegrationException {
        return new HttpUrl(String.format("https://us03-int-%s.nprd.sig.synopsys.com", value));
    }

}
