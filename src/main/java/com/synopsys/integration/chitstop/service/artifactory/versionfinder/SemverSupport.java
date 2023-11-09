/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;

public class SemverSupport {
    public boolean isValid(String version) {
        try {
            new Semver(version);
            return true;
        } catch (SemverException ignored) {
            return false;
        }
    }

    public Optional<Semver> fromStringSafely(String version) {
        return isValid(version) ? Optional.of(new Semver(version)) : Optional.empty();
    }

    /**
     * Filters out strings that don't match semver requirements.
     * @param versions
     * @return A sorted list, oldest to newest.
     */
    public List<Semver> filterVersions(List<String> versions) {
        return filter(versions)
                   .sorted()
                   .collect(Collectors.toList());
    }

    public Optional<Semver> latestVersion(List<String> versionStrings) {
        Stream<Semver> stream =
            filter(versionStrings);
        return sortAndGetLast(stream);
    }

    public Optional<Semver> latestWithinMajorVersion(List<String> versionStrings, int majorVersion) {
        Stream<Semver> stream =
            filter(versionStrings)
                .filter(semver -> majorVersion == semver.getMajor());
        return sortAndGetLast(stream);
    }

    private Optional<Semver> sortAndGetLast(Stream<Semver> stream) {
        return stream
                   .sorted()
                   .reduce((ignored, second) -> second);
    }

    private Stream<Semver> filter(List<String> versions) {
        return versions
                   .stream()
                   .map(this::fromStringSafely)
                   .flatMap(Optional::stream);
    }

}
