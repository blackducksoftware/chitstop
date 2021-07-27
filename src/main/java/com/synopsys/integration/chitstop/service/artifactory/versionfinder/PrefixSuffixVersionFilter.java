package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vdurmont.semver4j.Semver;

public class PrefixSuffixVersionFilter extends VersionFilter {
    private final String prefix;
    private final String suffix;

    public PrefixSuffixVersionFilter(SemverSupport semverSupport, String prefix, String suffix) {
        super(semverSupport);
        this.prefix = prefix;
        this.suffix = suffix;
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

    private List<String> getVersions(List<String> allVersions) {
        List<String> trimmedVersions = allVersions
                                           .stream()
                                           .map(version -> {
                                               int prefixIndex = version.indexOf(prefix);
                                               int suffixIndex = version.indexOf(suffix);
                                               if (suffixIndex > prefixIndex && prefixIndex > -1) {
                                                   return version.substring(prefixIndex + prefix.length(), suffixIndex);
                                               }
                                               return version;
                                           })
                                           .collect(Collectors.toList());
        return trimmedVersions;
    }

}
