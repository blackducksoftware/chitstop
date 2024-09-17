/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory;

import com.synopsys.integration.util.Stringable;

public class ArtifactoryFile extends Stringable {
    private final String repo;
    private final String path;
    private final String downloadUri;

    public ArtifactoryFile(String repo, String path, String downloadUri) {
        this.repo = repo;
        this.path = path;
        this.downloadUri = downloadUri;
    }

    public String getRepo() {
        return repo;
    }

    public String getPath() {
        return path;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

}
