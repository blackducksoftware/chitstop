/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synopsys.integration.chitstop.model.ApiToken;
import com.synopsys.integration.chitstop.model.ApiTokens;

@RestController
public class ApiTokenController {
    private final ApiTokens apiTokens;

    @Autowired
    public ApiTokenController(ApiTokens apiTokens) {
        this.apiTokens = apiTokens;
    }

    @GetMapping("/token")
    public ApiToken retrieve(
        @RequestParam(value = "vm") String vm,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "name", required = false) String name
    ) {
        return apiTokens.retrieve(vm, username, name);
    }

    @PostMapping("/token")
    public void store(@RequestBody ApiToken apiToken) {
        apiTokens.addToken(apiToken);
    }

    @GetMapping("/puretoken")
    public String retrievePure(
        @RequestParam(value = "vm") String vm,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "name", required = false) String name
    ) {
        return apiTokens.retrievePure(vm, username, name);
    }

}
