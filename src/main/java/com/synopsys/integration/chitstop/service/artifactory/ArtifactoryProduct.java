package com.synopsys.integration.chitstop.service.artifactory;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.ArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.VersionFinder;

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
