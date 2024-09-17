/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.rest.model;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class VmKeyTypeAdapter extends TypeAdapter<VmKey> {
    @Override
    public void write(JsonWriter jsonWriter, VmKey vmKey) throws IOException {
        jsonWriter.value(vmKey.getValue());
    }

    @Override
    public VmKey read(JsonReader jsonReader) throws IOException {
        String vmKey = jsonReader.nextString();
        return new VmKey(vmKey);
    }

}
