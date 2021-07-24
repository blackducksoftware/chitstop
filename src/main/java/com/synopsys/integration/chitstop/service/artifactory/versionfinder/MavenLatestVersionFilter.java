package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MavenLatestVersionFilter extends LatestVersionFilter {
    public MavenLatestVersionFilter(SemverSupport semverSupport) {
        super(semverSupport);
    }

    @Override
    public Optional<String> latest(List<String> allVersions) {
        /*
         * ejk 2021-07-22 versions from maven repos will be folder uris
         */
        List<String> trimmedVersions = allVersions
                                           .stream()
                                           .map(version -> version.startsWith("/") ? version.substring(1) : version)
                                           .collect(Collectors.toList());

        return semverSupport.latestVersion(trimmedVersions);
    }

}
