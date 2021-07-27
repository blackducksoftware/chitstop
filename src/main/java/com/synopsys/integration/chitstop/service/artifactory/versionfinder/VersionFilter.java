package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.List;
import java.util.Optional;

import com.vdurmont.semver4j.Semver;

public abstract class VersionFilter {
    protected SemverSupport semverSupport;

    public VersionFilter(SemverSupport semverSupport) {
        this.semverSupport = semverSupport;
    }

    public abstract Optional<Semver> latest(List<String> allVersions);

    public abstract Optional<Semver> latestWithinMajorVersion(List<String> allVersions, int majorVersion);

}
