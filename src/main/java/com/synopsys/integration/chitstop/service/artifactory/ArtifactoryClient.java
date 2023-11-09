/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.artifactory;

import java.io.IOException;
import java.util.Optional;

import com.google.gson.Gson;
import com.synopsys.integration.chitstop.rest.model.ArtifactoryPropertiesResponse;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.rest.HttpMethod;
import com.synopsys.integration.rest.HttpUrl;
import com.synopsys.integration.rest.client.IntHttpClient;
import com.synopsys.integration.rest.request.Request;
import com.synopsys.integration.rest.response.Response;

public class ArtifactoryClient {
    private final IntLogger logger;
    private final IntHttpClient httpClient;
    private final Gson gson;
    private final HttpUrl readOnlyArtifactory;
    private final HttpUrl writeArtifactory;

    public ArtifactoryClient(IntLogger logger, IntHttpClient httpClient, Gson gson, HttpUrl readOnlyArtifactory, HttpUrl writeArtifactory) {
        this.logger = logger;
        this.httpClient = httpClient;
        this.gson = gson;
        this.readOnlyArtifactory = readOnlyArtifactory;
        this.writeArtifactory = writeArtifactory;
    }

    public ArtifactoryPropertiesResponse findProperties(ArtifactoryPath artifactoryPath) throws IntegrationException {
        HttpUrl propertiesUrl = readOnlyArtifactory.appendRelativeUrl(String.format("api/storage/%s?properties", artifactoryPath.value()));
        return get(propertiesUrl, ArtifactoryPropertiesResponse.class);
    }

    public void putProperty(ArtifactoryPath artifactoryPath, String key, String value) throws IntegrationException {
        HttpUrl propertyUrl = writeArtifactory.appendRelativeUrl(String.format("api/storage/%s?properties=%s=%s&recursive=0", artifactoryPath.value(), key, value));
        put(propertyUrl);
    }

    public ArtifactoryItem findItem(ArtifactoryPath artifactoryPath) throws IntegrationException {
        HttpUrl itemInfo = readOnlyArtifactory.appendRelativeUrl(String.format("api/storage/%s", artifactoryPath.value()));
        return get(itemInfo, ArtifactoryItem.class);
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
        HttpUrl itemInfo = readOnlyArtifactory.appendRelativeUrl(String.format("api/storage/%s", artifactoryPath.value()));
        return get(itemInfo, ArtifactoryFile.class);
    }

    public Optional<ArtifactoryFile> findFileSafely(ArtifactoryPath artifactoryPath) {
        try {
            return Optional.ofNullable(findFile(artifactoryPath));
        } catch (IntegrationException e) {
            logger.error(String.format("Could not get the artifactory file for %s: %s", artifactoryPath.value(), e.getMessage()), e);
        }
        return Optional.empty();
    }

    private <T> T get(HttpUrl url, Class<T> responseClass) throws IntegrationException {
        Request request = new Request.Builder(url).build();
        try (Response response = httpClient.execute(request)) {
            String content = response.getContentString();

            return gson.fromJson(content, responseClass);
        } catch (IOException e) {
            throw new IntegrationException("Could not get a response from Artifactory: " + e.getMessage(), e);
        }
    }

    private void put(HttpUrl url) throws IntegrationException {
        Request request = new Request.Builder(url).method(HttpMethod.PUT).build();
        try (Response response = httpClient.execute(request)) {
        } catch (IOException e) {
            throw new IntegrationException("Could not execute PUT to Artifactory: " + e.getMessage(), e);
        }
    }

}
