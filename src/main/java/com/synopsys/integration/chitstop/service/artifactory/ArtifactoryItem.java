/*
 * chitstop
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.artifactory;

import java.util.List;

import com.synopsys.integration.util.Stringable;

public class ArtifactoryItem extends Stringable {
    private final String repo;
    private final String path;
    private final List<ArtifactoryChildItem> children;

    public ArtifactoryItem(String repo, String path, List<ArtifactoryChildItem> children) {
        this.repo = repo;
        this.path = path;
        this.children = children;
    }

    public String getRepo() {
        return repo;
    }

    public String getPath() {
        return path;
    }

    public List<ArtifactoryChildItem> getChildren() {
        return children;
    }

}
