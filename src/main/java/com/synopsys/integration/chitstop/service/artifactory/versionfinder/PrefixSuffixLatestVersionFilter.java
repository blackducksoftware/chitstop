package com.synopsys.integration.chitstop.service.artifactory.versionfinder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PrefixSuffixLatestVersionFilter extends LatestVersionFilter {
    private final String prefix;
    private final String suffix;

    public PrefixSuffixLatestVersionFilter(SemverSupport semverSupport, String prefix, String suffix) {
        super(semverSupport);
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public Optional<String> latest(List<String> allVersions) {
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
        return semverSupport.latestVersion(trimmedVersions);
    }

}
