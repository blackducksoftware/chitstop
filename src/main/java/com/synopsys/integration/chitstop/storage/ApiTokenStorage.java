/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synopsys.integration.chitstop.exception.GameOver;
import com.synopsys.integration.chitstop.model.ApiToken;

@Repository
public class ApiTokenStorage {
    public static final String TOKENS_FILENAME = "tokens.json";

    private final Logger logger = LoggerFactory.getLogger(ApiTokenStorage.class);

    @Value("${stored.tokens.directory}")
    private String storedTokensDirectory;

    private final WatchService watchService;
    private final ExecutorService executorService;
    private final ObjectMapper objectMapper;
    private final GameOver gameOver;

    private Path tokensJsonPath;
    private List<ApiToken> apiTokens;

    @Autowired
    public ApiTokenStorage(WatchService watchService, ExecutorService executorService, ObjectMapper objectMapper, GameOver gameOver) {
        this.watchService = watchService;
        this.executorService = executorService;
        this.objectMapper = objectMapper;
        this.gameOver = gameOver;
    }

    @PostConstruct
    public void init() {
        Path storedTokensPath = Paths.get(storedTokensDirectory);
        tokensJsonPath = storedTokensPath.resolve(TOKENS_FILENAME);
        loadApiTokens();

        try {
            storedTokensPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            gameOver.endIt(String.format("The storage path (%s) could not be used to register modify events. Check that the path exists and is a directory the application may access.", storedTokensPath));
        }

        executorService.submit(new ApiTokenWatcher(storedTokensPath, watchService, this, gameOver));
    }

    public void addApiToken(ApiToken apiToken) {
        List<ApiToken> currentTokens = new LinkedList<>(apiTokens);
        currentTokens.add(apiToken);
        try {
            String json = objectMapper.writeValueAsString(currentTokens);
            Files.writeString(tokensJsonPath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            gameOver.endIt(logger, e, String.format("Count not add the new api token: %s", apiToken));
        }

        loadApiTokens();
    }

    public List<ApiToken> retrieveApiTokens() {
        return apiTokens;
    }

    public void loadApiTokens() {
        try {
            String json = Files.readString(tokensJsonPath, StandardCharsets.UTF_8);
            apiTokens = objectMapper.readValue(json, new TypeReference<>() {});
        } catch (IOException e) {
            logger.error(String.format("Api tokens could not be loaded from the provided path: %s", tokensJsonPath.toString()), e);
        }
    }

}
