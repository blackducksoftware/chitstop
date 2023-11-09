/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.model;

public class VmTokensStatus {
    private final VmKey vmKey;
    private final String status;

    public static VmTokensStatus okay(VmKey vmKey) {
        return new VmTokensStatus(vmKey, "OK");
    }

    public static VmTokensStatus missing(VmKey vmKey) {
        return new VmTokensStatus(vmKey, "MISSING");
    }

    public VmTokensStatus(VmKey vmKey, String status) {
        this.vmKey = vmKey;
        this.status = status;
    }

    public VmKey getVmKey() {
        return vmKey;
    }

    public String getStatus() {
        return status;
    }

}
