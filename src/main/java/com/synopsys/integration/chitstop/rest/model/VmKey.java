/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.model;

import javax.validation.constraints.NotBlank;

import com.synopsys.integration.util.Stringable;

public class VmKey extends Stringable {
    @NotBlank
    private final String value;

    public VmKey(String vmKey) {
        value =
            vmKey
                .replace("https://", "")
                .replace("http://", "")
                .replace("us1a-int-", "")
                .replace(".nprd.sig.synopsys.com", "");
    }

    public String getValue() {
        return value;
    }

}
