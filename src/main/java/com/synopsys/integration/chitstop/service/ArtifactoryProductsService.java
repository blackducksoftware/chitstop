package com.synopsys.integration.chitstop.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.rest.model.ArtifactoryPropertiesResponse;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryPath;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProduct;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProducts;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

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
        ArtifactoryProduct product = artifactoryProducts.byName(productName);
        ArtifactoryProductDetails artifactoryProductDetails = product.getArtifactoryProductDetails();
        ArtifactoryPath artifactoryPath = ArtifactoryPath.createPropertiesPath(artifactoryProductDetails);

        ArtifactoryPropertiesResponse propertiesResponse = artifactoryClient.findProperties(artifactoryPath);
        Map<String, List<String>> propertyMap = propertiesResponse.getProperties();

        List<Pair<String, String>> properties = new LinkedList<>();
        for (String name : propertyMap.keySet()) {
            if (name.startsWith(artifactoryProductDetails.getPropertyPrefix())) {
                String values = StringUtils.join(propertyMap.get(name), ", ");
                properties.add(Pair.of(name, values));
            }
        }
        return properties;
    }

    public Optional<HttpUrl> getLatestArtifactUrl(String productName) throws IntegrationException {
        ArtifactoryProduct product = artifactoryProducts.byName(productName);
        ArtifactoryProductDetails artifactoryProductDetails = product.getArtifactoryProductDetails();

        ArtifactoryPath artifactsPath = ArtifactoryPath.createArtifactsPath(artifactoryProductDetails);
        Optional<String> optionalLatestVersion = product.getLatestVersionFinder().latest(artifactsPath);
        if (optionalLatestVersion.isEmpty()) {
            return Optional.empty();
        }

        String latestVersion = optionalLatestVersion.get();
        return product.getArtifactFinder().artifactByVersion(artifactoryProductDetails, latestVersion);
    }

}
