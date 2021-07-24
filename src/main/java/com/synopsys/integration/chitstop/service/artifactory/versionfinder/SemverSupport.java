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
                   .collect(Collectors.toList());
    }

    public Optional<String> latestVersion(List<String> versionStrings) {
        return filter(versionStrings)
                   .reduce((ignored, second) -> second)
                   .map(Semver::getValue);
    }

    private Stream<Semver> filter(List<String> versions) {
        return versions
                   .stream()
                   .map(this::fromStringSafely)
                   .flatMap(Optional::stream)
                   .sorted();
    }

}
