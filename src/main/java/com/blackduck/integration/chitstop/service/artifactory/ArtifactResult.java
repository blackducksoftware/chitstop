/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory;

import com.blackduck.integration.util.Stringable;

public class ArtifactResult extends Stringable {
    public static final ArtifactResult NOT_FOUND = new ArtifactResult("", "", "", false);

    private final String productName;
    private final String propertyKey;
    private final String downloadUrl;
    private final boolean updateRecommended;

    public ArtifactResult(String productName, String propertyKey, String downloadUrl, boolean updateRecommended) {
        this.productName = productName;
        this.propertyKey = propertyKey;
        this.downloadUrl = downloadUrl;
        this.updateRecommended = updateRecommended;
    }

    public String getProductName() {
        return productName;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public boolean isUpdateRecommended() {
        return updateRecommended;
    }

}
