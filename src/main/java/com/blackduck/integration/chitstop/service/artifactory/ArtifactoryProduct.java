/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory;

import com.blackduck.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.blackduck.integration.chitstop.service.artifactory.artifactfinder.ArtifactFinder;
import com.blackduck.integration.chitstop.service.artifactory.versionfinder.VersionFinder;

public class ArtifactoryProduct {
    private final ArtifactoryProductDetails artifactoryProductDetails;
    private final VersionFinder versionFinder;
    private final ArtifactFinder artifactFinder;

    public ArtifactoryProduct(ArtifactoryProductDetails artifactoryProductDetails, VersionFinder versionFinder, ArtifactFinder artifactFinder) {
        this.artifactoryProductDetails = artifactoryProductDetails;
        this.versionFinder = versionFinder;
        this.artifactFinder = artifactFinder;
    }

    public ArtifactoryProductDetails getArtifactoryProductDetails() {
        return artifactoryProductDetails;
    }

    public VersionFinder getVersionFinder() {
        return versionFinder;
    }

    public ArtifactFinder getArtifactFinder() {
        return artifactFinder;
    }

}
