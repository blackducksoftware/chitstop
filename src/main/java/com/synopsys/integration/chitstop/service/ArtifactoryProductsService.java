package com.synopsys.integration.chitstop.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.rest.model.ArtifactoryPropertiesResponse;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactResult;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryPath;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProduct;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProducts;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.ArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.VersionFinder;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;
import com.vdurmont.semver4j.Semver;

@Service
public class ArtifactoryProductsService {
    private final ArtifactoryProducts artifactoryProducts;
    private final ArtifactoryClient artifactoryClient;

    @Autowired
    public ArtifactoryProductsService(ArtifactoryProducts artifactoryProducts, ArtifactoryClient artifactoryClient) {
        this.artifactoryProducts = artifactoryProducts;
        this.artifactoryClient = artifactoryClient;
    }

    public List<ArtifactoryProductDetails> findAll() {
        return artifactoryProducts
                   .list()
                   .stream()
                   .map(ArtifactoryProduct::getArtifactoryProductDetails)
                   .collect(Collectors.toList());
    }

    public Map<ArtifactoryProductDetails, List<Pair<String, String>>> findAllProperties() throws IntegrationException {
        Map<ArtifactoryProductDetails, List<Pair<String, String>>> allProperties = new LinkedHashMap<>();
        for (ArtifactoryProductDetails artifactoryProductDetails : findAll()) {
            allProperties.put(artifactoryProductDetails, findProperties(artifactoryProductDetails.getName()));
        }

        return allProperties;
    }

    public List<Pair<String, String>> findProperties(String productName) throws IntegrationException {
        Optional<ArtifactoryProduct> optionalProduct = artifactoryProducts.byName(productName);
        if (optionalProduct.isEmpty()) {
            return Collections.emptyList();
        }
        ArtifactoryProduct product = optionalProduct.get();

        ArtifactoryProductDetails artifactoryProductDetails = product.getArtifactoryProductDetails();
        ArtifactoryPath artifactoryPath = ArtifactoryPath.createPropertiesPath(artifactoryProductDetails);

        ArtifactoryPropertiesResponse propertiesResponse = artifactoryClient.findProperties(artifactoryPath);
        Map<String, List<String>> propertyMap = propertiesResponse.getProperties();
        if (null == propertyMap) {
            return Collections.emptyList();
        }

        List<Pair<String, String>> properties = new LinkedList<>();
        for (String name : propertyMap.keySet()) {
            if (name.startsWith(artifactoryProductDetails.getPropertyPrefix())) {
                String values = StringUtils.join(propertyMap.get(name), ", ");
                properties.add(Pair.of(name, values));
            }
        }
        return properties;
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
        artifactoryClient.putProperty(artifactsPath, artifactResult.getPropertyKey(), artifactResult.getDownloadUrl().string());
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
        return Optional.of(new ArtifactResult(productName, propertyKey, downloadUrl));
    }

}
