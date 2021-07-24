package com.synopsys.integration.chitstop.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.chitstop.ChitstopApplication;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProducts;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.NestedArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.RepoArtifactFinder;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

public class ArtifactoryProductsServiceTest {
    @Test
    public void testArtifactory() throws IntegrationException {
        ChitstopApplication chitstopApplication = new ChitstopApplication();
        ArtifactoryClient artifactoryClient = chitstopApplication.artifactoryClient();
        ArtifactoryProducts artifactoryProducts = new ArtifactoryProducts(
            chitstopApplication.detectFontBundleLatestVersionFinder(),
            chitstopApplication.mavenLatestVersionFinder(),
            chitstopApplication.nugetLatestVersionFinder(),
            new RepoArtifactFinder(artifactoryClient),
            new NestedArtifactFinder(artifactoryClient)
        );

        ArtifactoryProductsService artifactoryProductsService = new ArtifactoryProductsService(artifactoryProducts, artifactoryClient);
        Optional<HttpUrl> url = artifactoryProductsService.getLatestArtifactUrl("integrationNugetInspector");
        assertTrue(url.isPresent());
        System.out.println(url.get().string());
    }

}
