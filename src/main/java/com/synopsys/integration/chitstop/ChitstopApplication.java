/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.synopsys.integration.chitstop.exception.GameOverException;

@SpringBootApplication
public class ChitstopApplication {
    private static final Logger logger = LoggerFactory.getLogger(ChitstopApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(ChitstopApplication.class, args);
        } catch (GameOverException e) {
            logger.error("An unrecoverable error occurred.", e);
        }
    }

    @Bean
    public WatchService watchService() throws IOException {
        return FileSystems.getDefault().newWatchService();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                   .configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @Bean
    /*
    ejk - at the moment, we only need one thread for the ApiTokenWatcher, if we
    need more, we'll need to use a different ExecutorService implementation
     */
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

}
