package com.synopsys.integration.chitstop.rest.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.service.ArtifactoryProductsService;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

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
        return artifactoryProductsService.findAll();
    }

    @GetMapping(
        value = "/products/latest",
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ResponseBody
    public String retrieve(@RequestParam(value = "name") String productName) throws IntegrationException {
        return artifactoryProductsService
                   .getLatestArtifactUrl(productName)
                   .map(HttpUrl::string)
                   .orElse("Not Found");
    }

    @GetMapping(
        value = "/properties/all",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<ArtifactoryProductDetails, List<Pair<String, String>>> retrieveArtifactoryProperties() throws IntegrationException {
        return artifactoryProductsService.findAllProperties();
    }

}
