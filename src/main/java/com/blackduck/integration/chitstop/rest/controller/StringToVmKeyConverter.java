/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.rest.controller;

import org.springframework.core.convert.converter.Converter;

import com.blackduck.integration.chitstop.rest.model.VmKey;

public class StringToVmKeyConverter implements Converter<String, VmKey> {
    @Override
    public VmKey convert(String source) {
        return new VmKey(source);
    }

}
