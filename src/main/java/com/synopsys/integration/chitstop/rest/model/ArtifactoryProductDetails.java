/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.model;

import com.synopsys.integration.util.Stringable;

public class ArtifactoryProductDetails extends Stringable {
    private final String name;
    private final String repoKey;
    private final String propertiesItemPath;
    private final String artifactsItemPath;
    private final String propertyPrefix;
    private final String artifactSuffix;

    public ArtifactoryProductDetails(String name, String repoKey, String propertiesItemPath, String artifactsItemPath, String propertyPrefix, String artifactSuffix) {
        this.name = name;
        this.repoKey = repoKey;
        this.propertiesItemPath = propertiesItemPath;
        this.artifactsItemPath = artifactsItemPath;
        this.propertyPrefix = propertyPrefix + "_LATEST_";
        this.artifactSuffix = artifactSuffix;
    }

    public String getName() {
        return name;
    }

    public String getRepoKey() {
        return repoKey;
    }

    public String getPropertiesItemPath() {
        return propertiesItemPath;
    }

    public String getArtifactsItemPath() {
        return artifactsItemPath;
    }

    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    public String getArtifactSuffix() {
        return artifactSuffix;
    }

}
