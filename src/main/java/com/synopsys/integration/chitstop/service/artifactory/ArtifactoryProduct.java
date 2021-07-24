package com.synopsys.integration.chitstop.service.artifactory;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.ArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.LatestVersionFinder;

public class ArtifactoryProduct {
    private final ArtifactoryProductDetails artifactoryProductDetails;
    private final LatestVersionFinder latestVersionFinder;
    private final ArtifactFinder artifactFinder;

    public ArtifactoryProduct(ArtifactoryProductDetails artifactoryProductDetails, LatestVersionFinder latestVersionFinder, ArtifactFinder artifactFinder) {
        this.artifactoryProductDetails = artifactoryProductDetails;
        this.latestVersionFinder = latestVersionFinder;
        this.artifactFinder = artifactFinder;
    }

    public ArtifactoryProductDetails getArtifactoryProductDetails() {
        return artifactoryProductDetails;
    }

    public LatestVersionFinder getLatestVersionFinder() {
        return latestVersionFinder;
    }

    public ArtifactFinder getArtifactFinder() {
        return artifactFinder;
    }

}
