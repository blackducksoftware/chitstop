/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import springfox.documentation.spring.web.json.Json;

public class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
    @Override
    public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
        return JsonParser.parseString(json.value());
    }

}
