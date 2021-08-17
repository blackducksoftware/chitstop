/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.utility;

import java.util.Optional;

import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

public class HttpUrlCreator {
    public static Optional<HttpUrl> createSafely(String url) {
        try {
            return Optional.of(new HttpUrl(url));
        } catch (IntegrationException ignored) {
            return Optional.empty();
        }
    }

}
