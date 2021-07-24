package com.synopsys.integration.chitstop.service.artifactory;

import java.io.IOException;
import java.util.Optional;

import com.google.gson.Gson;
import com.synopsys.integration.chitstop.rest.model.ArtifactoryPropertiesResponse;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.HttpUrl;
import com.synopsys.integration.rest.client.IntHttpClient;
import com.synopsys.integration.rest.request.Request;
import com.synopsys.integration.rest.response.Response;

public class ArtifactoryClient {
    public static final String PUBLIC_ARTIFACTORY = "https://sig-repo.synopsys.com/artifactory";
    public static final String INTERNAL_ARTIFACTORY = "https://artifactory.internal.synopsys.com/artifactory";

    private final IntLogger logger;
    private final IntHttpClient httpClient;
    private final Gson gson;

    public ArtifactoryClient(IntLogger logger, IntHttpClient httpClient, Gson gson) {
        this.logger = logger;
        this.httpClient = httpClient;
        this.gson = gson;
    }

    public ArtifactoryPropertiesResponse findProperties(ArtifactoryPath artifactoryPath) throws IntegrationException {
        HttpUrl propertiesUrl = new HttpUrl(String.format("%s/api/storage/%s?properties", PUBLIC_ARTIFACTORY, artifactoryPath.value()));
        return find(propertiesUrl, ArtifactoryPropertiesResponse.class);
    }

    public ArtifactoryItem findItem(ArtifactoryPath artifactoryPath) throws IntegrationException {
        HttpUrl itemInfo = new HttpUrl(String.format("%s/api/storage/%s", PUBLIC_ARTIFACTORY, artifactoryPath.value()));
        return find(itemInfo, ArtifactoryItem.class);
    }

    public Optional<ArtifactoryItem> findItemSafely(ArtifactoryPath artifactoryPath) {
        try {
            return Optional.ofNullable(findItem(artifactoryPath));
        } catch (IntegrationException e) {
            logger.error(String.format("Could not get the artifactory item for %s: %s", artifactoryPath.value(), e.getMessage()), e);
        }
        return Optional.empty();
    }

    public ArtifactoryFile findFile(ArtifactoryPath artifactoryPath) throws IntegrationException {
        HttpUrl itemInfo = new HttpUrl(String.format("%s/api/storage/%s", PUBLIC_ARTIFACTORY, artifactoryPath.value()));
        return find(itemInfo, ArtifactoryFile.class);
    }

    public Optional<ArtifactoryFile> findFileSafely(ArtifactoryPath artifactoryPath) {
        try {
            return Optional.ofNullable(findFile(artifactoryPath));
        } catch (IntegrationException e) {
            logger.error(String.format("Could not get the artifactory file for %s: %s", artifactoryPath.value(), e.getMessage()), e);
        }
        return Optional.empty();
    }

    private <T> T find(HttpUrl url, Class<T> responseClass) throws IntegrationException {
        Request request = new Request.Builder(url).build();
        try (Response response = httpClient.execute(request)) {
            String content = response.getContentString();

            return gson.fromJson(content, responseClass);
        } catch (IOException e) {
            throw new IntegrationException("Could not get a response from Artifactory: " + e.getMessage(), e);
        }
    }

}
