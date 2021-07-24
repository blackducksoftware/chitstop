package com.synopsys.integration.chitstop.service.artifactory;

import com.synopsys.integration.util.Stringable;

public class ArtifactoryChildItem extends Stringable {
    private final String uri;
    private final boolean folder;

    public ArtifactoryChildItem(String uri, boolean folder) {
        this.uri = uri;
        this.folder = folder;
    }

    public String getUri() {
        return uri;
    }

    public boolean isFolder() {
        return folder;
    }

}
