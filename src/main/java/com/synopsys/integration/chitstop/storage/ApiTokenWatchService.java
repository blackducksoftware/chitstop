/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiTokenWatchService {
    @Value("${stored.tokens.directory}")
    private String storedTokensDirectory;

    private final WatchService watchService;
    private final ApiTokenPopulator apiTokenPopulator;

    @Autowired
    public ApiTokenWatchService(WatchService watchService, ApiTokenPopulator apiTokenPopulator) throws IOException {
        this.watchService = watchService;
        this.apiTokenPopulator = apiTokenPopulator;
    }

    @PostConstruct
    public void createWatcher() throws IOException {
        Path storedTokensPath = new File(storedTokensDirectory).toPath();
        apiTokenPopulator.populateApiTokens(storedTokensPath.resolve("tokens.json"));

        storedTokensPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        new Thread(new ApiTokenWatcher(storedTokensPath, watchService, apiTokenPopulator)).start();
    }

}
