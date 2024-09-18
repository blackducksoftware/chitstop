/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.artifactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.blackduck.integration.chitstop.rest.model.ArtifactoryProperty;

public class LatestPropertySelectorTest {
    @Test
    public void testSorting() {
        Map<String, List<String>> propertyMap = new HashMap<>();
        propertyMap.put("TEST_LATEST_6", List.of("snickerdoodle"));
        propertyMap.put("TEST_LATEST_2", List.of("fig newton"));
        // but...a cookie's just a cookie...a newton...???
        propertyMap.put("TEST_LATEST_3", List.of("oreo"));
        propertyMap.put("TEST_LATEST_5", List.of("chocolate chip"));
        propertyMap.put("not allowed", List.of("oatmeal raisin"));
        propertyMap.put("TEST_LATEST_1", List.of("molasses"));

        LatestPropertySelector latestPropertySelector = new LatestPropertySelector();
        List<ArtifactoryProperty> properties = latestPropertySelector.selectLatestProperties(propertyMap, "TEST_LATEST_");
        assertEquals(5, properties.size());

        List<String> expected = List.of("snickerdoodle", "chocolate chip", "oreo", "fig newton", "molasses");
        assertEquals(expected, properties.stream().map(ArtifactoryProperty::getValue).collect(Collectors.toList()));
    }

}
