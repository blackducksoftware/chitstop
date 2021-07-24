package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import org.springframework.beans.factory.annotation.Autowired;

public class NugetLatestVersionFilter extends PrefixSuffixLatestVersionFilter {
    public static final String PREFIX = "Inspector.";
    public static final String SUFFIX = ".nupkg";

    @Autowired
    public NugetLatestVersionFilter(SemverSupport semverSupport) {
        /*
         * ejk 2021-07-22 versions from nuget repos all look like:
         * '...Inspector.SEMVER.nupkg', e.g. /NugetDotnet3Inspector.1.0.1.nupkg
         */
        super(semverSupport, PREFIX, SUFFIX);
    }

}
