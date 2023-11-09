/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.model;

public class BlackDuckVmStatus {
    private final String blackDuckUrl;
    private final String version;
    private final boolean ready;
    private final boolean live;

    public BlackDuckVmStatus(String blackDuckUrl, String version, boolean ready, boolean live) {
        this.blackDuckUrl = blackDuckUrl;
        this.version = version;
        this.ready = ready;
        this.live = live;
    }

    public String getBlackDuckUrl() {
        return blackDuckUrl;
    }

    public String getVersion() {
        return version;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isLive() {
        return live;
    }

}
