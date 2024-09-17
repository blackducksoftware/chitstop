/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory;

import java.util.List;

import com.blackduck.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.blackduck.integration.chitstop.service.artifactory.artifactfinder.NestedArtifactFinder;
import com.blackduck.integration.chitstop.service.artifactory.artifactfinder.RepoArtifactFinder;
import com.blackduck.integration.chitstop.service.artifactory.versionfinder.VersionFinder;

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
                "BD Nuget Inspector (Sunsetting)",
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
                "Integration Nuget Inspector (Sunsetting)",
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
                "dotnet3 Nuget Inspector (Sunsetting)",
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
                "dotnet5 Nuget Inspector (Sunsetting)",
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
                "Detect",
                "bds-integrations-release",
                "com/blackduck/integration/synopsys-detect",
                "com/blackduck/integration/synopsys-detect",
                "DETECT",
                ".jar"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DETECT_NUGET_INSPECTOR_LINUX =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "Detect Nuget Inspector LINUX",
                "bds-integrations-release",
                "com/blackduck/integration/synopsys-detect",
                "com/blackduck/integration/detect-nuget-inspector",
                "NUGET_INSPECTOR_LINUX",
                "-linux.zip"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DETECT_NUGET_INSPECTOR_MAC =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "Detect Nuget Inspector MAC",
                "bds-integrations-release",
                "com/blackduck/integration/synopsys-detect",
                "com/blackduck/integration/detect-nuget-inspector",
                "NUGET_INSPECTOR_MAC",
                "-mac.zip"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DETECT_NUGET_INSPECTOR_WINDOWS =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "Detect Nuget Inspector WINDOWS",
                "bds-integrations-release",
                "com/blackduck/integration/synopsys-detect",
                "com/blackduck/integration/detect-nuget-inspector",
                "NUGET_INSPECTOR_WINDOWS",
                "-windows.zip"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DETECT_FONT_BUNDLE =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "Detect Font Bundle",
                "bds-integrations-release",
                "com/blackduck/integration/synopsys-detect",
                "com/blackduck/integration/integration-resources/fonts",
                "DETECT_FONT_BUNDLE",
                ".zip"),
                detectFontBundleVersionFinder,
                repoArtifactFinder
            );

        ArtifactoryProduct DOCKER_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "Docker Inspector",
                "bds-integrations-release",
                "com/blackduck/integration/blackduck-docker-inspector",
                "com/blackduck/integration/blackduck-docker-inspector",
                "DOCKER_INSPECTOR",
                ".jar"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DOCKER_AIR_GAP_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "Docker AirGap Inspector",
                "bds-integrations-release",
                "com/blackduck/integration/blackduck-docker-inspector",
                "com/blackduck/integration/blackduck-docker-inspector",
                "DOCKER_INSPECTOR_AIR_GAP",
                "-air-gap.zip"),
                mavenVersionFinder,
                nestedArtifactFinder
            );

        ArtifactoryProduct DETECT_TEST =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "detectTest",
                "bds-integrations-test",
                "com/blackduck/integration/synopsys-detect",
                "com/blackduck/integration/synopsys-detect",
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
            DETECT,
            DETECT_NUGET_INSPECTOR_LINUX,
            DETECT_NUGET_INSPECTOR_MAC,
            DETECT_NUGET_INSPECTOR_WINDOWS,
            DETECT_FONT_BUNDLE,
            DOCKER_INSPECTOR,
            DOCKER_AIR_GAP_INSPECTOR,
            BLACKDUCK_NUGET_INSPECTOR,
            INTEGRATION_NUGET_INSPECTOR,
            DOTNET3_NUGET_INSPECTOR,
            DOTNET5_NUGET_INSPECTOR
        );

        return new ArtifactoryProducts(artifactoryProducts);
    }

}
