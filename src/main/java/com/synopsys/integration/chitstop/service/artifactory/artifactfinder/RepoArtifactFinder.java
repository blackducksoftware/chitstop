/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.artifactory.artifactfinder;

import java.util.Optional;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryChildItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryFile;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryPath;
import com.synopsys.integration.chitstop.service.utility.HttpUrlCreator;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

public class RepoArtifactFinder implements ArtifactFinder {
    private final ArtifactoryClient artifactoryClient;

    public RepoArtifactFinder(ArtifactoryClient artifactoryClient) {
        this.artifactoryClient = artifactoryClient;
    }

    @Override
    public Optional<HttpUrl> artifactByVersion(ArtifactoryProductDetails artifactoryProductDetails, String version) throws IntegrationException {
        ArtifactoryPath artifactoryPath = ArtifactoryPath.createArtifactsPath(artifactoryProductDetails);

        ArtifactoryItem artifactoryItem = artifactoryClient.findItem(artifactoryPath);
        return artifactoryItem
                   .getChildren()
                   .stream()
                   .filter(child -> !child.isFolder())
                   .filter(ITEM_MATCHES.apply(artifactoryProductDetails, version))
                   .map(ArtifactoryChildItem::getUri)
                   .map(artifactoryPath::append)
                   .map(artifactoryClient::findFileSafely)
                   .flatMap(Optional::stream)
                   .map(ArtifactoryFile::getDownloadUri)
                   .map(HttpUrlCreator::createSafely)
                   .flatMap(Optional::stream)
                   .findFirst();
    }

}
