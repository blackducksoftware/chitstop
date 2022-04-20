/*
 * chitstop
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.model;

import java.util.List;
import java.util.Map;

public class ArtifactoryPropertiesResponse {
    private final String uri;
    private final Map<String, List<String>> properties;

    public ArtifactoryPropertiesResponse(String uri, Map<String, List<String>> properties) {
        this.uri = uri;
        this.properties = properties;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }

}
