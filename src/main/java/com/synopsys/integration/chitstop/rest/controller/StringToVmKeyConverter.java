/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.controller;

import org.springframework.core.convert.converter.Converter;

import com.synopsys.integration.chitstop.rest.model.VmKey;

public class StringToVmKeyConverter implements Converter<String, VmKey> {
    @Override
    public VmKey convert(String source) {
        return new VmKey(source);
    }

}
