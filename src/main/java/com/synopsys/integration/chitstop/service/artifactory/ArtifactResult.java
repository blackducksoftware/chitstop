package com.synopsys.integration.chitstop.service.artifactory;

import com.synopsys.integration.util.Stringable;

public class ArtifactResult extends Stringable {
    public static final ArtifactResult NOT_FOUND = new ArtifactResult("", "", "");

    private final String productName;
    private final String propertyKey;
    private final String downloadUrl;

    public ArtifactResult(String productName, String propertyKey, String downloadUrl) {
        this.productName = productName;
        this.propertyKey = propertyKey;
        this.downloadUrl = downloadUrl;
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

}
