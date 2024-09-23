/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.utility;

import java.util.Optional;

import com.blackduck.integration.exception.IntegrationException;
import com.blackduck.integration.rest.HttpUrl;

public class HttpUrlCreator {
    public static Optional<HttpUrl> createSafely(String url) {
        try {
            return Optional.of(new HttpUrl(url));
        } catch (IntegrationException ignored) {
            return Optional.empty();
        }
    }

}
