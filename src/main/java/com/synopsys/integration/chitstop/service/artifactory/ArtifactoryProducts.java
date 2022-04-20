/*
 * chitstop
 *
 * Copyright (c) 2022 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.artifactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArtifactoryProducts {
    private final Map<String, ArtifactoryProduct> artifactoryProducts;

    public ArtifactoryProducts(List<ArtifactoryProduct> artifactoryProducts) {
        this.artifactoryProducts = artifactoryProducts
                                       .stream()
                                       .collect(Collectors.toMap(product -> product.getArtifactoryProductDetails().getName(), Function.identity(), (a, b) -> a, LinkedHashMap::new));
    }

    public List<ArtifactoryProduct> list() {
        return new ArrayList<>(artifactoryProducts.values());
    }

    public Optional<ArtifactoryProduct> byName(String name) {
        return Optional.ofNullable(artifactoryProducts.get(name));
    }

}
