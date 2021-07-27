package com.synopsys.integration.chitstop.service.artifactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.chitstop.service.artifactory.versionfinder.NugetVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.SemverSupport;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.VersionFilter;
import com.vdurmont.semver4j.Semver;

public class NugetVersionFinderTest {
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
        VersionFilter versionFilter = new NugetVersionFilter(semverSupport);
        Optional<String> latest = versionFilter
                                      .latest(BLACKDUCK)
                                      .map(Semver::getValue);
        assertEquals("1.0.2", latest.get());
    }

    @Test
    public void testFindingLatestIntegration() {
        VersionFilter versionFilter = new NugetVersionFilter(semverSupport);
        Optional<String> latest = versionFilter
                                      .latest(INTEGRATION)
                                      .map(Semver::getValue);
        assertEquals("3.0.1", latest.get());
    }

    @Test
    public void testFindingLatestDotnet3() {
        VersionFilter versionFilter = new NugetVersionFilter(semverSupport);
        Optional<String> latest = versionFilter
                                      .latest(DOTNET3)
                                      .map(Semver::getValue);
        assertEquals("1.0.1", latest.get());
    }

    @Test
    public void testFindingLatestDotnet5() {
        VersionFilter versionFilter = new NugetVersionFilter(semverSupport);
        Optional<String> latest = versionFilter
                                      .latest(DOTNET5)
                                      .map(Semver::getValue);
        assertEquals("1.0.1", latest.get());
    }

}
