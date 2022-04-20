/*
 * chitstop
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.vms;

import java.util.List;

import org.springframework.stereotype.Component;

import com.synopsys.integration.chitstop.rest.model.BlackDuckVmStatus;

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
