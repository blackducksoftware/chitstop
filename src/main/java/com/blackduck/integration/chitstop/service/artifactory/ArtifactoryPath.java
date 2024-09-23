/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory;

import com.blackduck.integration.chitstop.rest.model.ArtifactoryProductDetails;

public class ArtifactoryPath {
    public static ArtifactoryPath createPropertiesPath(ArtifactoryProductDetails details) {
        return new ArtifactoryPath(String.format("%s/%s", details.getRepoKey(), details.getPropertiesItemPath()));
    }

    public static ArtifactoryPath createArtifactsPath(ArtifactoryProductDetails details) {
        return new ArtifactoryPath(String.format("%s/%s", details.getRepoKey(), details.getArtifactsItemPath()));
    }

    public static ArtifactoryPath create(String repoKey, String itemPath) {
        return new ArtifactoryPath(String.format("%s/%s", repoKey, itemPath));
    }

    private final String path;

    public ArtifactoryPath(String path) {
        this.path = path;
    }

    public String value() {
        return path;
    }

    public ArtifactoryPath append(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return new ArtifactoryPath(String.format("%s/%s", this.path, path));
    }

}
