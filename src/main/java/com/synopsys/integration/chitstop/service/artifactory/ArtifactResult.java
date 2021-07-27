package com.synopsys.integration.chitstop.service.artifactory;

import com.synopsys.integration.rest.HttpUrl;
import com.synopsys.integration.util.Stringable;

public class ArtifactResult extends Stringable {
    public static final ArtifactResult NOT_FOUND = new ArtifactResult("", "", null);

    private final String productName;
    private final String propertyKey;
    private final HttpUrl downloadUrl;

    public ArtifactResult(String productName, String propertyKey, HttpUrl downloadUrl) {
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

    public HttpUrl getDownloadUrl() {
        return downloadUrl;
    }

}
