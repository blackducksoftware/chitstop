package com.synopsys.integration.chitstop.service.artifactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.chitstop.service.artifactory.versionfinder.LatestVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.MavenLatestVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.SemverSupport;

public class MavenLatestVersionFilterTest {
    public static final List<String> VERSIONS = List.of("/0fonts", "/5.1.0",
        "/5.3.0", "/5.3.1", "/5.3.2", "/5.3.3", "/5.4.0", "/5.5.0", "/5.5.1",
        "/5.6.1", "/5.6.2", "/6.0.0", "/6.0.0-RC4", "/6.1.0", "/6.1.0-RC4",
        "/6.2.0", "/6.2.1", "/6.3.0", "/6.4.0", "/6.4.1", "/6.4.2", "/6.5.0",
        "/6.6.0", "/6.7.0", "/6.8.0", "/6.9.0", "/6.9.1", "/7.0.0", "/7.1.0");

    private final SemverSupport semverSupport = new SemverSupport();

    @Test
    public void testFindingLatestVersion() {
        LatestVersionFilter latestVersionFilter = new MavenLatestVersionFilter(semverSupport);
        Optional<String> latestVersion = latestVersionFilter.latest(VERSIONS);
        assertEquals("7.1.0", latestVersion.get());
    }

    @Test
    public void testFindingLatestVersionWithPatches() {
        LatestVersionFilter latestVersionFilter = new MavenLatestVersionFilter(semverSupport);
        Optional<String> latestVersion = latestVersionFilter.latest(List.of("1.0.0", "2.2.1", "2.2.0", "1.10.10"));
        assertEquals("2.2.1", latestVersion.get());
    }

    @Test
    public void testNonsenseIsIgnored() {
        LatestVersionFilter latestVersionFilter = new MavenLatestVersionFilter(semverSupport);
        Optional<String> latestVersion = latestVersionFilter.latest(List.of("nonsense", "10.10.001", "10.10.2", "9.15.0"));
        assertEquals("10.10.2", latestVersion.get());
    }

    @Test
    public void testOnlyNonsense() {
        LatestVersionFilter latestVersionFilter = new MavenLatestVersionFilter(semverSupport);
        Optional<String> latestVersion = latestVersionFilter.latest(List.of("this", "is", "just", "ridiculous"));
        assertTrue(latestVersion.isEmpty());
    }

}
