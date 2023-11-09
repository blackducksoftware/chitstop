/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.rest.model.ArtifactoryProperty;
import com.synopsys.integration.chitstop.service.ArtifactoryProductsService;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactResult;
import com.synopsys.integration.exception.IntegrationException;

@RestController
@RequestMapping("/api/artifactory")
public class ArtifactoryProductController {
    private final ArtifactoryProductsService artifactoryProductsService;

    @Autowired
    public ArtifactoryProductController(ArtifactoryProductsService artifactoryProductsService) {
        this.artifactoryProductsService = artifactoryProductsService;
    }

    @GetMapping(
        value = "/products/all",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<ArtifactoryProductDetails> retrieveArtifactoryProducts() {
        return artifactoryProductsService.findAllProducts();
    }

    @GetMapping(
        value = "/products",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ArtifactoryProductDetails retrieveArtifactoryProduct(
        @RequestParam(value = "name"
        ) String productName) throws IntegrationException {
        return artifactoryProductsService
                   .findProduct(productName)
                   .orElse(null);
    }

    @GetMapping(
        value = "/products/latest",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ArtifactResult retrieve(
        @RequestParam(value = "name") String productName,
        @RequestParam(value = "majorVersion", required = false) Integer majorVersion
    ) throws IntegrationException {
        if (null == majorVersion) {
            return artifactoryProductsService
                       .getLatestVersionArtifactResult(productName)
                       .orElse(ArtifactResult.NOT_FOUND);
        } else {
            return artifactoryProductsService
                       .getLatestWithinMajorVersionArtifactResult(productName, majorVersion)
                       .orElse(ArtifactResult.NOT_FOUND);
        }
    }

    @PutMapping(
        value = "/products/latest",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public void updateArtifact(@RequestBody ArtifactResult artifactResult) throws IntegrationException {
        artifactoryProductsService.updateProperty(artifactResult);
    }

    @GetMapping(
        value = "/properties/all",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<ArtifactoryProductDetails, List<ArtifactoryProperty>> retrieveArtifactoryProperties() throws IntegrationException {
        return artifactoryProductsService.findAllProperties();
    }

    @GetMapping(
        value = "/properties",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<ArtifactoryProperty> retrieveArtifactoryProductProperties(
        @RequestParam(value = "name"
        ) String productName) throws IntegrationException {
        return artifactoryProductsService.findProperties(productName);
    }

}
