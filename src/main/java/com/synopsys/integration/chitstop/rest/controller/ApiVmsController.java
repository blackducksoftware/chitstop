/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.synopsys.integration.chitstop.rest.model.BlackDuckVmStatus;
import com.synopsys.integration.chitstop.service.vms.VmDiagnostics;

@RestController
@RequestMapping("/api/vms")
public class ApiVmsController {
    private final VmDiagnostics vmDiagnostics;

    @Autowired
    public ApiVmsController(VmDiagnostics vmDiagnostics) {
        this.vmDiagnostics = vmDiagnostics;
    }

    @GetMapping(
        value = "/diagnostics",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<BlackDuckVmStatus> diagnostics(
        @RequestParam(value = "forceRefreshCache", required = false) String forceRefreshCache
    ) {
        return vmDiagnostics.findAllStatuses(Boolean.parseBoolean(forceRefreshCache));
    }

}
