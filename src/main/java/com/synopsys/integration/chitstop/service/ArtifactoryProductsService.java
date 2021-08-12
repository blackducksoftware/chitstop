package com.synopsys.integration.chitstop.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.rest.model.ArtifactoryPropertiesResponse;
import com.synopsys.integration.chitstop.rest.model.ArtifactoryProperty;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactResult;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryPath;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProduct;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProducts;
import com.synopsys.integration.chitstop.service.artifactory.LatestPropertySelector;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.ArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.VersionFinder;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;
import com.vdurmont.semver4j.Semver;

@Service
public class ArtifactoryProductsService {
    private final ArtifactoryProducts artifactoryProducts;
    private final ArtifactoryClient artifactoryClient;
    private final LatestPropertySelector latestPropertySelector;

    @Autowired
    public ArtifactoryProductsService(ArtifactoryProducts artifactoryProducts, ArtifactoryClient artifactoryClient, LatestPropertySelector latestPropertySelector) {
        this.artifactoryProducts = artifactoryProducts;
        this.artifactoryClient = artifactoryClient;
        this.latestPropertySelector = latestPropertySelector;
    }

    public List<ArtifactoryProductDetails> findAllProducts() {
        return artifactoryProducts
                   .list()
                   .stream()
                   .map(ArtifactoryProduct::getArtifactoryProductDetails)
                   .collect(Collectors.toList());
    }

    public Optional<ArtifactoryProductDetails> findProduct(String productName) {
        return artifactoryProducts
                   .byName(productName)
                   .map(ArtifactoryProduct::getArtifactoryProductDetails);
    }

    public Map<ArtifactoryProductDetails, List<ArtifactoryProperty>> findAllProperties() throws IntegrationException {
        Map<ArtifactoryProductDetails, List<ArtifactoryProperty>> allProperties = new LinkedHashMap<>();
        for (ArtifactoryProductDetails artifactoryProductDetails : findAllProducts()) {
            allProperties.put(artifactoryProductDetails, findProperties(artifactoryProductDetails.getName()));
        }

        return allProperties;
    }

    public List<ArtifactoryProperty> findProperties(String productName) throws IntegrationException {
        Optional<ArtifactoryProduct> optionalProduct = artifactoryProducts.byName(productName);
        if (optionalProduct.isEmpty()) {
            return Collections.emptyList();
        }
        ArtifactoryProduct product = optionalProduct.get();

        ArtifactoryProductDetails artifactoryProductDetails = product.getArtifactoryProductDetails();
        ArtifactoryPath artifactoryPath = ArtifactoryPath.createPropertiesPath(artifactoryProductDetails);

        ArtifactoryPropertiesResponse propertiesResponse = artifactoryClient.findProperties(artifactoryPath);
        Map<String, List<String>> propertyMap = propertiesResponse.getProperties();

        return latestPropertySelector.selectLatestProperties(propertyMap, artifactoryProductDetails.getPropertyPrefix());
    }

    public Optional<ArtifactResult> getLatestVersionArtifactResult(String productName) throws IntegrationException {
        return getArtifactResult(productName, (artifactoryPath, versionFinder) -> versionFinder.latest(artifactoryPath));
    }

    public Optional<ArtifactResult> getLatestWithinMajorVersionArtifactResult(String productName, int majorVersion) throws IntegrationException {
        return getArtifactResult(productName, (artifactoryPath, versionFinder) -> versionFinder.latestWithinMajorVersion(artifactoryPath, majorVersion));
    }

    public void updateProperty(ArtifactResult artifactResult) throws IntegrationException {
        Optional<ArtifactoryProduct> optionalProduct = artifactoryProducts.byName(artifactResult.getProductName());
        if (optionalProduct.isEmpty()) {
            return;
        }
        ArtifactoryProduct product = optionalProduct.get();

        ArtifactoryProductDetails artifactoryProductDetails = product.getArtifactoryProductDetails();
        ArtifactoryPath artifactsPath = ArtifactoryPath.createArtifactsPath(artifactoryProductDetails);
        artifactoryClient.putProperty(artifactsPath, artifactResult.getPropertyKey(), artifactResult.getDownloadUrl());
    }

    private Optional<ArtifactResult> getArtifactResult(String productName, BiFunction<ArtifactoryPath, VersionFinder, Optional<Semver>> versionFinderUsage) throws IntegrationException {
        Optional<ArtifactoryProduct> optionalProduct = artifactoryProducts.byName(productName);
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        ArtifactoryProduct product = optionalProduct.get();

        ArtifactoryProductDetails artifactoryProductDetails = product.getArtifactoryProductDetails();
        ArtifactoryPath artifactsPath = ArtifactoryPath.createArtifactsPath(artifactoryProductDetails);
        VersionFinder versionFinder = product.getVersionFinder();

        Optional<Semver> optionalLatestVersion = versionFinderUsage.apply(artifactsPath, versionFinder);
        if (optionalLatestVersion.isEmpty()) {
            return Optional.empty();
        }
        Semver latestVersion = optionalLatestVersion.get();

        ArtifactFinder artifactFinder = product.getArtifactFinder();
        Optional<HttpUrl> optionalDownloadUrl = artifactFinder.artifactByVersion(artifactoryProductDetails, latestVersion.getValue());
        if (optionalDownloadUrl.isEmpty()) {
            return Optional.empty();
        }
        HttpUrl downloadUrl = optionalDownloadUrl.get();

        String propertyKey = artifactoryProductDetails.getPropertyPrefix() + latestVersion.getMajor();

        List<ArtifactoryProperty> artifactoryProperties = findProperties(productName);
        boolean updateRecommended = !doesPropertyHaveValue(artifactoryProperties, propertyKey, downloadUrl.string());

        return Optional.of(new ArtifactResult(productName, propertyKey, downloadUrl.string(), updateRecommended));
    }

    private boolean doesPropertyHaveValue(List<ArtifactoryProperty> artifactoryProperties, String propertyKey, String propertyValue) {
        return artifactoryProperties
                   .stream()
                   .filter(property -> propertyKey.equals(property.getKey()) && propertyValue.equals(property.getValue()))
                   .findAny()
                   .isPresent();
    }

}
