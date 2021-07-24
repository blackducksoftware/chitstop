package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.List;
import java.util.Optional;

public abstract class LatestVersionFilter {
    protected SemverSupport semverSupport;

    public LatestVersionFilter(SemverSupport semverSupport) {
        this.semverSupport = semverSupport;
    }

    public abstract Optional<String> latest(List<String> allVersions);

}
