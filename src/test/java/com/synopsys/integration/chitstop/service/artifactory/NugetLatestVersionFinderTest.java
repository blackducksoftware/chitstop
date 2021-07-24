package com.synopsys.integration.chitstop.service.artifactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.chitstop.service.artifactory.versionfinder.LatestVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.NugetLatestVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.SemverSupport;

public class NugetLatestVersionFinderTest {
    public static final List<String> BLACKDUCK =
        List.of("/BlackduckNugetInspector.0.0.11.nupkg",
            "/BlackduckNugetInspector.0.0.12.nupkg",
            "/BlackduckNugetInspector.0.0.13.nupkg",
            "/BlackduckNugetInspector.0.0.4.nupkg",
            "/BlackduckNugetInspector.0.0.6.nupkg",
            "/BlackduckNugetInspector.0.0.7.nupkg",
            "/BlackduckNugetInspector.0.0.8.nupkg",
            "/BlackduckNugetInspector.0.0.9.nupkg",
            "/BlackduckNugetInspector.1.0.0.nupkg",
            "/BlackduckNugetInspector.1.0.1.nupkg",
            "/BlackduckNugetInspector.1.0.2.nupkg"
        );

    public static final List<String> INTEGRATION =
        List.of("/IntegrationNugetInspector.2.5.1.nupkg",
            "/IntegrationNugetInspector.2.5.2.nupkg",
            "/IntegrationNugetInspector.2.5.3.nupkg",
            "/IntegrationNugetInspector.2.5.4.nupkg",
            "/IntegrationNugetInspector.2.5.6.nupkg",
            "/IntegrationNugetInspector.2.5.7.nupkg",
            "/IntegrationNugetInspector.3.0.0.nupkg",
            "/IntegrationNugetInspector.3.0.1.nupkg");

    public static final List<String> DOTNET3 =
        List.of("/NugetDotnet3Inspector.0.0.1.nupkg",
            "/NugetDotnet3Inspector.1.0.0.nupkg",
            "/NugetDotnet3Inspector.1.0.1.nupkg");

    public static final List<String> DOTNET5 = List.of("/NugetDotnet5Inspector.1.0.1.nupkg");

    private final SemverSupport semverSupport = new SemverSupport();

    @Test
    public void testFindingLatestBlackduck() {
        LatestVersionFilter latestVersionFilter = new NugetLatestVersionFilter(semverSupport);
        Optional<String> latest = latestVersionFilter.latest(BLACKDUCK);
        assertEquals("1.0.2", latest.get());
    }

    @Test
    public void testFindingLatestIntegration() {
        LatestVersionFilter latestVersionFilter = new NugetLatestVersionFilter(semverSupport);
        Optional<String> latest = latestVersionFilter.latest(INTEGRATION);
        assertEquals("3.0.1", latest.get());
    }

    @Test
    public void testFindingLatestDotnet3() {
        LatestVersionFilter latestVersionFilter = new NugetLatestVersionFilter(semverSupport);
        Optional<String> latest = latestVersionFilter.latest(DOTNET3);
        assertEquals("1.0.1", latest.get());
    }

    @Test
    public void testFindingLatestDotnet5() {
        LatestVersionFilter latestVersionFilter = new NugetLatestVersionFilter(semverSupport);
        Optional<String> latest = latestVersionFilter.latest(DOTNET5);
        assertEquals("1.0.1", latest.get());
    }

}
