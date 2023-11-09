/*
 * chitstop
 *
 * Copyright (c) 2023 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.artifactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProperty;

@Component
public class LatestPropertySelector {
    public List<ArtifactoryProperty> selectLatestProperties(Map<String, List<String>> propertyMap, String propertyPrefix) {
        if (null == propertyMap) {
            return Collections.emptyList();
        }

        List<ArtifactoryProperty> properties = new LinkedList<>();
        for (String name : propertyMap.keySet()) {
            if (name.startsWith(propertyPrefix)) {
                String values = propertyMap.get(name).get(0);
                properties.add(new ArtifactoryProperty(name, values));
            }
        }

        Function<String, Integer> extractVersion = s -> Integer.parseInt(s.replace(propertyPrefix, ""));
        Collections.sort(properties, (a, b) -> {
            int aVersion = extractVersion.apply(a.getKey());
            int bVersion = extractVersion.apply(b.getKey());
            return Integer.compare(bVersion, aVersion);
        });

        return properties;
    }

}
