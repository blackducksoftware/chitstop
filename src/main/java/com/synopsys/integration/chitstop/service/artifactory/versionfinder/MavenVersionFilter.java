package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.vdurmont.semver4j.Semver;

public class MavenVersionFilter extends VersionFilter {
    public MavenVersionFilter(SemverSupport semverSupport) {
        super(semverSupport);
    }

    @Override
    public Optional<Semver> latest(List<String> allVersions) {
        List<String> trimmedVersions = getVersions(allVersions);

        return semverSupport.latestVersion(trimmedVersions);
    }

    @Override
    public Optional<Semver> latestWithinMajorVersion(List<String> allVersions, int majorVersion) {
        List<String> trimmedVersions = getVersions(allVersions);

        return semverSupport.latestWithinMajorVersion(trimmedVersions, majorVersion);
    }

    @NotNull
    private List<String> getVersions(List<String> allVersions) {
        /*
         * ejk 2021-07-22 versions from maven repos will be folder uris
         */
        return allVersions
                   .stream()
                   .map(version -> version.startsWith("/") ? version.substring(1) : version)
                   .collect(Collectors.toList());
    }

}
