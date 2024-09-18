/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.blackduck.integration.chitstop.ChitstopApplication;
import com.blackduck.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactResult;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryProduct;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryProducts;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryProductsFactory;
import com.blackduck.integration.chitstop.service.artifactory.LatestPropertySelector;
import com.blackduck.integration.chitstop.service.artifactory.artifactfinder.ArtifactFinder;
import com.blackduck.integration.chitstop.service.artifactory.artifactfinder.NestedArtifactFinder;
import com.blackduck.integration.chitstop.service.artifactory.versionfinder.VersionFinder;
import com.blackduck.integration.exception.IntegrationException;
import com.blackduck.integration.log.IntLogger;
import com.blackduck.integration.rest.HttpUrl;
import com.blackduck.integration.rest.client.IntHttpClient;
import com.google.gson.Gson;

public class ArtifactoryProductsServiceTest {
    @Test
    @Disabled
    public void testArtifactory() throws IntegrationException {
        ChitstopApplication chitstopApplication = new ChitstopApplication();
        ArtifactoryClient artifactoryClient = chitstopApplication.artifactoryClient();
        ArtifactoryProductsFactory artifactoryProductsFactory = chitstopApplication.artifactoryProductsFactory();
        LatestPropertySelector latestPropertySelector = new LatestPropertySelector();
        ArtifactoryProducts artifactoryProducts = artifactoryProductsFactory.createDefault();

        ArtifactoryProductsService artifactoryProductsService = new ArtifactoryProductsService(artifactoryProducts, artifactoryClient, latestPropertySelector);
        Optional<ArtifactResult> artifactResult = artifactoryProductsService.getLatestVersionArtifactResult("integrationNugetInspector");
        assertTrue(artifactResult.isPresent());
        System.out.println(artifactResult.get());
    }

    @Test
    @Disabled
    public void testCreatingProperty() throws IntegrationException {
        ChitstopApplication chitstopApplication = new ChitstopApplication();

        IntLogger logger = chitstopApplication.logger();
        IntHttpClient httpClient = chitstopApplication.httpClient();
        Gson gson = chitstopApplication.gson();
        HttpUrl internalArtifactory = new HttpUrl("https://artifactory.internal.synopsys.com/artifactory");
        ArtifactoryClient artifactoryClient = new ArtifactoryClient(logger, httpClient, gson, internalArtifactory, internalArtifactory);
        VersionFinder mavenVersionFinder = new VersionFinder(artifactoryClient, chitstopApplication.mavenLatestVersionFilter());
        ArtifactFinder nestedArtifactFinder = new NestedArtifactFinder(artifactoryClient);

        ArtifactoryProductDetails details = new ArtifactoryProductDetails("detectTest", "bds-integrations-test",
            "com/blackduck/integration/synopsys-detect",
            "com/blackduck/integration/synopsys-detect", "DETECT_TEST", ".jar");
        ArtifactoryProduct detectTest = new ArtifactoryProduct(details, mavenVersionFinder, nestedArtifactFinder);
        LatestPropertySelector latestPropertySelector = new LatestPropertySelector();
        ArtifactoryProducts artifactoryProducts = new ArtifactoryProducts(List.of(detectTest));

        ArtifactoryProductsService artifactoryProductsService = new ArtifactoryProductsService(artifactoryProducts, artifactoryClient, latestPropertySelector);
        ArtifactResult artifactResult = artifactoryProductsService.getLatestVersionArtifactResult("detectTest").get();
        ArtifactResult artifactResult6 = artifactoryProductsService.getLatestWithinMajorVersionArtifactResult("detectTest", 6).get();

        printProperties(artifactoryProductsService, "detectTest");

        artifactoryProductsService.updateProperty(artifactResult);
        artifactoryProductsService.updateProperty(artifactResult6);

        printProperties(artifactoryProductsService, "detectTest");
    }

    private void printProperties(ArtifactoryProductsService artifactoryProductsService, String productName) throws IntegrationException {
        artifactoryProductsService
            .findProperties(productName)
            .stream()
            .map(property -> String.format("%s = %s", property.getKey(), property.getValue()))
            .forEach(System.out::println);
    }

}
