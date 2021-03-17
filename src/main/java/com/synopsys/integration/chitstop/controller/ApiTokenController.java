/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ApiToken retrieveByUsername(
        @RequestParam(value = "vm") String vm,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "name", required = false) String tokenName
    ) {
        if (StringUtils.isNotBlank(tokenName)) {
            return apiTokens.findByVMAndTokenName(vm, tokenName);
        } else if (StringUtils.isNotBlank(username)) {
            return apiTokens.findByVMAndUsername(vm ,username);
        } else {
            return apiTokens.findByVM(vm);
        }
    }

}
