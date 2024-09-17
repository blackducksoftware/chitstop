/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.vms;

import java.util.List;

import org.springframework.stereotype.Component;

import com.blackduck.integration.chitstop.rest.model.BlackDuckVmStatus;

@Component
public class VmStatusCache {
    private List<BlackDuckVmStatus> statuses;

    public boolean isEmpty() {
        return null == statuses || statuses.isEmpty();
    }

    public List<BlackDuckVmStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<BlackDuckVmStatus> statuses) {
        this.statuses = statuses;
    }

}
