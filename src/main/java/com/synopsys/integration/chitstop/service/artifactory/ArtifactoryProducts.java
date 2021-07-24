package com.synopsys.integration.chitstop.service.artifactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.NestedArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.RepoArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.LatestVersionFinder;

@Component
public class ArtifactoryProducts {
    private final Map<String, ArtifactoryProduct> artifactoryProducts;

    @Autowired
    public ArtifactoryProducts(
        LatestVersionFinder detectFontBundleLatestVersionFinder,
        LatestVersionFinder mavenLatestVersionFinder,
        LatestVersionFinder nugetLatestVersionFinder,
        RepoArtifactFinder repoArtifactFinder,
        NestedArtifactFinder nestedArtifactFinder) {

        ArtifactoryProduct BLACKDUCK_NUGET_INSPECTOR =
            new ArtifactoryProduct(new ArtifactoryProductDetails(
                "blackduckNugetInspector",
                "bds-integrations-nuget-release",
                "BlackduckNugetInspector",
                "BlackduckNugetInspector",
                "NUGET_INSPECTOR",
                ".nupkg"),
                nugetLatestVersionFinder,
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
                nugetLatestVersionFinder,
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
                nugetLatestVersionFinder,
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
                nugetLatestVersionFinder,
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
                mavenLatestVersionFinder,
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
                detectFontBundleLatestVersionFinder,
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
                mavenLatestVersionFinder,
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
                mavenLatestVersionFinder,
                nestedArtifactFinder
            );

        artifactoryProducts = List.of(
            BLACKDUCK_NUGET_INSPECTOR,
            INTEGRATION_NUGET_INSPECTOR,
            DOTNET3_NUGET_INSPECTOR,
            DOTNET5_NUGET_INSPECTOR,
            DETECT,
            DETECT_FONT_BUNDLE,
            DOCKER_INSPECTOR,
            DOCKER_AIR_GAP_INSPECTOR
        )
                                  .stream()
                                  .collect(Collectors.toMap(product -> product.getArtifactoryProductDetails().getName(), Function.identity(), (a, b) -> a, LinkedHashMap::new));
    }

    public List<ArtifactoryProduct> list() {
        return new ArrayList<>(artifactoryProducts.values());
    }

    public ArtifactoryProduct byName(String name) {
        return artifactoryProducts.get(name);
    }

}
