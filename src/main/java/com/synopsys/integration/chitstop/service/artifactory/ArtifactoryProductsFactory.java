package com.synopsys.integration.chitstop.service.artifactory;

import java.util.List;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.NestedArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.RepoArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.VersionFinder;

public class ArtifactoryProductsFactory {
    private final VersionFinder detectFontBundleVersionFinder;
    private final VersionFinder mavenVersionFinder;
    private final VersionFinder nugetVersionFinder;
    private final RepoArtifactFinder repoArtifactFinder;
    private final NestedArtifactFinder nestedArtifactFinder;

    public ArtifactoryProductsFactory(VersionFinder detectFontBundleVersionFinder, VersionFinder mavenVersionFinder,
        VersionFinder nugetVersionFinder, RepoArtifactFinder repoArtifactFinder, NestedArtifactFinder nestedArtifactFinder) {
        this.detectFontBundleVersionFinder = detectFontBundleVersionFinder;
        this.mavenVersionFinder = mavenVersionFinder;
        this.nugetVersionFinder = nugetVersionFinder;
        this.repoArtifactFinder = repoArtifactFinder;
        this.nestedArtifactFinder = nestedArtifactFinder;
    }

    public ArtifactoryProducts createDefault() {
        ArtifactoryProduct BLACKDUCK_NUGET_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "blackduckNugetInspector",
                "bds-integrations-nuget-release",
                "BlackduckNugetInspector",
                "BlackduckNugetInspector",
                "NUGET_INSPECTOR",
                ".nupkg"),
                nugetVersionFinder,
                repoArtifactFinder
            );

        ArtifactoryProduct INTEGRATION_NUGET_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "integrationNugetInspector",
                "bds-integrations-nuget-release",
                "IntegrationNugetInspector",
                "IntegrationNugetInspector",
                "NUGET_INSPECTOR",
                ".nupkg"),
                nugetVersionFinder,
                repoArtifactFinder
            );

        ArtifactoryProduct DOTNET3_NUGET_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "dotnet3NugetInspector",
                "bds-integrations-nuget-release",
                "NugetDotnet3Inspector",
                "NugetDotnet3Inspector",
                "NUGET_DOTNET3_INSPECTOR",
                ".nupkg"),
                nugetVersionFinder,
                repoArtifactFinder
            );

        ArtifactoryProduct DOTNET5_NUGET_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "dotnet5NugetInspector",
                "bds-integrations-nuget-release",
                "NugetDotnet5Inspector",
                "NugetDotnet5Inspector",
                "NUGET_DOTNET5_INSPECTOR",
                ".nupkg"),
                nugetVersionFinder,
                repoArtifactFinder
            );

        ArtifactoryProduct DETECT =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "detect",
                "bds-integrations-release",
                "com/synopsys/integration/synopsys-detect",
                "com/synopsys/integration/synopsys-detect",
                "DETECT",
                ".jar"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DETECT_FONT_BUNDLE =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "detectFontBundle",
                "bds-integrations-release",
                "com/synopsys/integration/synopsys-detect",
                "com/synopsys/integration/integration-resources/fonts",
                "DETECT_FONT_BUNDLE",
                ".zip"),
                detectFontBundleVersionFinder,
                repoArtifactFinder
            );

        ArtifactoryProduct DOCKER_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "dockerInspector",
                "bds-integrations-release",
                "com/synopsys/integration/blackduck-docker-inspector",
                "com/synopsys/integration/blackduck-docker-inspector",
                "DOCKER_INSPECTOR",
                ".jar"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DOCKER_AIR_GAP_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "dockerAirGapInspector",
                "bds-integrations-release",
                "com/synopsys/integration/blackduck-docker-inspector",
                "com/synopsys/integration/blackduck-docker-inspector",
                "DOCKER_INSPECTOR_AIR_GAP",
                "-air-gap.zip"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DETECT_TEST =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "detectTest",
                "bds-integrations-test",
                "com/synopsys/integration/synopsys-detect",
                "com/synopsys/integration/synopsys-detect",
                "DETECT_TEST",
                ".jar"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct NUGET_TEST =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "nugetTest",
                "bds-integrations-nuget-snapshot",
                "NugetDotnet3Inspector",
                "NugetDotnet3Inspector",
                "NUGET_TEST",
                ".nupkg"),
                nugetVersionFinder,
                repoArtifactFinder
            );

        List<ArtifactoryProduct> artifactoryProducts = List.of(
            BLACKDUCK_NUGET_INSPECTOR,
            INTEGRATION_NUGET_INSPECTOR,
            DOTNET3_NUGET_INSPECTOR,
            DOTNET5_NUGET_INSPECTOR,
            DETECT,
            DETECT_FONT_BUNDLE,
            DOCKER_INSPECTOR,
            DOCKER_AIR_GAP_INSPECTOR,
            DETECT_TEST,
            NUGET_TEST
        );

        return new ArtifactoryProducts(artifactoryProducts);
    }

}
