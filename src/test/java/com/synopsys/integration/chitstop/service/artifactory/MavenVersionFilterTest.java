package com.synopsys.integration.chitstop.service.artifactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.chitstop.service.artifactory.versionfinder.MavenVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.SemverSupport;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.VersionFilter;
import com.vdurmont.semver4j.Semver;

public class MavenVersionFilterTest {
    public static final List<String> VERSIONS = List.of("/0fonts", "/5.1.0",
        "/5.3.0", "/5.3.1", "/5.3.2", "/5.3.3", "/5.4.0", "/5.5.0", "/5.5.1",
        "/5.6.1", "/5.6.2", "/6.0.0", "/6.0.0-RC4", "/6.1.0", "/6.1.0-RC4",
        "/6.2.0", "/6.2.1", "/6.3.0", "/6.4.0", "/6.4.1", "/6.4.2", "/6.5.0",
        "/6.6.0", "/6.7.0", "/6.8.0", "/6.9.0", "/6.9.1", "/7.0.0", "/7.1.0");

    private final SemverSupport semverSupport = new SemverSupport();

    @Test
    public void testFindingLatestVersion() {
        VersionFilter versionFilter = new MavenVersionFilter(semverSupport);
        Optional<String> latestVersion = versionFilter
                                             .latest(VERSIONS)
                                             .map(Semver::getValue);
        assertEquals("7.1.0", latestVersion.get());
    }

    @Test
    public void testFindingLatestVersionWithPatches() {
        VersionFilter versionFilter = new MavenVersionFilter(semverSupport);
        Optional<String> latestVersion = versionFilter
                                             .latest(List.of("1.0.0", "2.2.1", "2.2.0", "1.10.10"))
                                             .map(Semver::getValue);
        assertEquals("2.2.1", latestVersion.get());
    }

    @Test
    public void testNonsenseIsIgnored() {
        VersionFilter versionFilter = new MavenVersionFilter(semverSupport);
        Optional<String> latestVersion = versionFilter
                                             .latest(List.of("nonsense", "10.10.001", "10.10.2", "9.15.0"))
                                             .map(Semver::getValue);
        assertEquals("10.10.2", latestVersion.get());
    }

    @Test
    public void testOnlyNonsense() {
        VersionFilter versionFilter = new MavenVersionFilter(semverSupport);
        Optional<String> latestVersion = versionFilter
                                             .latest(List.of("this", "is", "just", "ridiculous"))
                                             .map(Semver::getValue);
        assertTrue(latestVersion.isEmpty());
    }

}
