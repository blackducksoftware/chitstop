/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory.artifactfinder;

import java.util.Optional;

import com.blackduck.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryChildItem;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryFile;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryItem;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryPath;
import com.blackduck.integration.chitstop.service.utility.HttpUrlCreator;
import com.blackduck.integration.exception.IntegrationException;
import com.blackduck.integration.rest.HttpUrl;

public class NestedArtifactFinder implements ArtifactFinder {
    private final ArtifactoryClient artifactoryClient;

    public NestedArtifactFinder(ArtifactoryClient artifactoryClient) {
        this.artifactoryClient = artifactoryClient;
    }

    @Override
    public Optional<HttpUrl> artifactByVersion(ArtifactoryProductDetails artifactoryProductDetails, String version) throws IntegrationException {
        ArtifactoryPath productPath = ArtifactoryPath.createArtifactsPath(artifactoryProductDetails);

        ArtifactoryPath folderPath = productPath.append(version);

        ArtifactoryItem folderItem = artifactoryClient.findItem(folderPath);

        return
            folderItem
                .getChildren()
                .stream()
                .filter(child -> !child.isFolder())
                .filter(ITEM_MATCHES.apply(artifactoryProductDetails, version))
                .findFirst()
                .map(ArtifactoryChildItem::getUri)
                .map(folderPath::append)
                .flatMap(artifactoryClient::findFileSafely)
                .map(ArtifactoryFile::getDownloadUri)
                .flatMap(HttpUrlCreator::createSafely);
    }

}
