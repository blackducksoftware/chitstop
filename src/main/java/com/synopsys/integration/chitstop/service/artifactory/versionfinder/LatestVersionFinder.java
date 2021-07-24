package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryChildItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryPath;

public class LatestVersionFinder {
    private final ArtifactoryClient artifactoryClient;
    private final LatestVersionFilter latestVersionFilter;

    public LatestVersionFinder(ArtifactoryClient artifactoryClient, LatestVersionFilter latestVersionFilter) {
        this.artifactoryClient = artifactoryClient;
        this.latestVersionFilter = latestVersionFilter;
    }

    public Optional<String> latest(ArtifactoryPath artifactoryPath) {
        List<String> allVersions =
            artifactoryClient
                .findItemSafely(artifactoryPath)
                .map(ArtifactoryItem::getChildren)
                .orElse(Collections.emptyList())
                .stream()
                .map(ArtifactoryChildItem::getUri)
                .collect(Collectors.toList());

        return latestVersionFilter.latest(allVersions);
    }

}
