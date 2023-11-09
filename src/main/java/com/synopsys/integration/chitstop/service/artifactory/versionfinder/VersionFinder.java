/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryChildItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryPath;
import com.vdurmont.semver4j.Semver;

public class VersionFinder {
    private final ArtifactoryClient artifactoryClient;
    private final VersionFilter versionFilter;

    public VersionFinder(ArtifactoryClient artifactoryClient, VersionFilter versionFilter) {
        this.artifactoryClient = artifactoryClient;
        this.versionFilter = versionFilter;
    }

    public Optional<Semver> latest(ArtifactoryPath artifactoryPath) {
        List<String> allVersions = getAllVersions(artifactoryPath);

        return versionFilter.latest(allVersions);
    }

    public Optional<Semver> latestWithinMajorVersion(ArtifactoryPath artifactoryPath, int majorVersion) {
        List<String> allVersions = getAllVersions(artifactoryPath);

        return versionFilter.latestWithinMajorVersion(allVersions, majorVersion);
    }

    @NotNull
    private List<String> getAllVersions(ArtifactoryPath artifactoryPath) {
        Optional<ArtifactoryItem> optionalItem = artifactoryClient.findItemSafely(artifactoryPath);
        if (optionalItem.isEmpty()) {
            return Collections.emptyList();
        }

        ArtifactoryItem item = optionalItem.get();

        return item
                   .getChildren()
                   .stream()
                   .map(ArtifactoryChildItem::getUri)
                   .collect(Collectors.toList());
    }

}
