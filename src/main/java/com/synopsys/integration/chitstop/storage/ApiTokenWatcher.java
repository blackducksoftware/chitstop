/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiTokenWatcher implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ApiTokenWatcher.class);

    private final Path watchedPath;
    private final WatchService watchService;
    private final ApiTokenPopulator apiTokenPopulator;

    public ApiTokenWatcher(Path watchedPath, WatchService watchService, ApiTokenPopulator apiTokenPopulator) {
        this.watchedPath = watchedPath;
        this.watchService = watchService;
        this.apiTokenPopulator = apiTokenPopulator;
    }

    @Override
    public void run() {
        while (true) {
            WatchKey watchKey = null;
            try {
                watchKey = watchService.take();
                logger.info("taking a watch key");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                logger.info("event: " + event.toString());
                if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
                    WatchEvent<Path> pathWatchEvent = (WatchEvent<Path>) event;
                    try {
                        logger.info(pathWatchEvent.context().toString() + "**");
                        apiTokenPopulator.populateApiTokens(watchedPath.resolve(pathWatchEvent.context()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            watchKey.reset();
        }
    }

}
