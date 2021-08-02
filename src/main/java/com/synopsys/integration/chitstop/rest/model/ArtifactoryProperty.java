package com.synopsys.integration.chitstop.rest.model;

public class ArtifactoryProperty {
    private final String key;
    private final String value;

    public ArtifactoryProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
