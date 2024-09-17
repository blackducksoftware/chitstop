/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory.artifactfinder;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.blackduck.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.blackduck.integration.chitstop.service.artifactory.ArtifactoryChildItem;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

public interface ArtifactFinder {
    BiFunction<ArtifactoryProductDetails, String, Predicate<ArtifactoryChildItem>> ITEM_MATCHES =
        (details, version) ->
            (childItem) ->
                childItem.getUri().endsWith(version + details.getArtifactSuffix());

    Optional<HttpUrl> artifactByVersion(ArtifactoryProductDetails artifactoryProductDetails, String version) throws IntegrationException;

}
