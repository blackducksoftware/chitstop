/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synopsys.integration.chitstop.rest.model.ApiToken;
import com.synopsys.integration.chitstop.rest.model.VmKey;
import com.synopsys.integration.chitstop.service.ApiTokens;

@RestController
public class ApiTokenController {
    private final ApiTokens apiTokens;

    @Autowired
    public ApiTokenController(ApiTokens apiTokens) {
        this.apiTokens = apiTokens;
    }

    @GetMapping("/puretoken")
    public String retrievePure(
        @RequestParam(value = "vm") VmKey vmKey,
        @RequestParam(value = "username", required = false) String username
    ) {
        return apiTokens.findPure(vmKey, username);
    }

    @GetMapping("/token")
    public ApiToken retrieve(
        @RequestParam(value = "vm") VmKey vmKey,
        @RequestParam(value = "username", required = false) String username
    ) {
        return apiTokens.find(vmKey, username);
    }

    @GetMapping("/tokens")
    public List<ApiToken> retrieveVmTokens(@RequestParam(value = "vm", required = false) VmKey vmKey) {
        return apiTokens.findAll(vmKey);
    }

    @GetMapping("/report")
    public String tokensReport() {
        return apiTokens.tokensStatusReport();
    }

    @PutMapping("/token")
    public void store(@RequestBody ApiToken apiToken) {
        apiTokens.storeToken(apiToken);
    }

    @DeleteMapping("/token")
    public void clearVmToken(
        @RequestParam(value = "vm") VmKey vmKey,
        @RequestParam(value = "token") String token
    ) {
        apiTokens.deleteVmToken(vmKey, token);
    }

    @DeleteMapping("/tokens")
    public void clearVmTokens(@RequestParam(value = "vm") VmKey vmKey) {
        apiTokens.deleteVmTokens(vmKey);
    }

}
