/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory.versionfinder;

import org.springframework.beans.factory.annotation.Autowired;

public class NugetVersionFilter extends PrefixSuffixVersionFilter {
    public static final String PREFIX = "Inspector.";
    public static final String SUFFIX = ".nupkg";

    @Autowired
    public NugetVersionFilter(SemverSupport semverSupport) {
        /*
         * ejk 2021-07-22 versions from nuget repos all look like:
         * '...Inspector.SEMVER.nupkg', e.g. /NugetDotnet3Inspector.1.0.1.nupkg
         */
        super(semverSupport, PREFIX, SUFFIX);
    }

}
