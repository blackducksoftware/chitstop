/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackduck.integration.chitstop.ApplicationProperties;
import com.blackduck.integration.chitstop.exception.GameOver;
import com.blackduck.integration.chitstop.rest.model.ApiToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Repository
public class ApiTokenStorage {
    public static final String TOKENS_FILENAME = "tokens.json";

    public static final Comparator<ApiToken> BY_VM_KEY = Comparator.comparing(apiToken -> apiToken.getTokenDetails().getVmKey().getValue());
    public static final Comparator<ApiToken> BY_USERNAME = Comparator.comparing(apiToken -> apiToken.getTokenDetails().getUsername());
    public static final Comparator<ApiToken> BY_SCOPE = Comparator.comparing(apiToken -> apiToken.getTokenDetails().getScope());
    public static final Comparator<ApiToken> BY_TOKEN = Comparator.comparing(ApiToken::getToken);

    public static final Type apiTokenListType = new TypeToken<LinkedList<ApiToken>>() {}.getType();

    private final Logger logger = LoggerFactory.getLogger(ApiTokenStorage.class);

    private final Path tokensJsonPath;
    private final WatchService watchService;
    private final ExecutorService executorService;
    private final Gson gson;
    private final GameOver gameOver;

    private final TreeSet<ApiToken> apiTokens = new TreeSet<>(
        BY_VM_KEY
            .thenComparing(BY_USERNAME)
            .thenComparing(BY_SCOPE)
            .thenComparing(BY_TOKEN)
    );

    @Autowired
    public ApiTokenStorage(ApplicationProperties applicationProperties, WatchService watchService, ExecutorService executorService, Gson gson, GameOver gameOver) {
        tokensJsonPath = Paths.get(applicationProperties.getStoredTokensPath());
        this.watchService = watchService;
        this.executorService = executorService;
        this.gson = gson;
        this.gameOver = gameOver;
    }

    @PostConstruct
    public void init() {
        loadApiTokens();

        Path watchedDirectory = tokensJsonPath.getParent();
        try {
            watchedDirectory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            gameOver.endIt(String.format("The storage path (%s) could not be used to register modify events. Check that the path exists and is a directory the application may access.", watchedDirectory));
        }

        ApiTokenWatcher apiTokenWatcher = new ApiTokenWatcher(tokensJsonPath.getFileName().toString(), watchService, this, gameOver);
        executorService.submit(apiTokenWatcher);
    }

    public void loadApiTokens() {
        try {
            String json = Files.readString(tokensJsonPath, StandardCharsets.UTF_8);
            apiTokens.clear();
            List<ApiToken> apiTokensFromFile = gson.fromJson(json, apiTokenListType);
            apiTokens.addAll(apiTokensFromFile);
        } catch (IOException e) {
            logger.error(String.format("Api tokens could not be loaded from the provided path: %s", tokensJsonPath.toString()), e);
        }
    }

    public void storeApiToken(ApiToken apiToken) {
        apiTokens
            .stream()
            .filter((token) -> token.sameDetails(apiToken))
            .findFirst()
            .ifPresent(apiTokens::remove);

        apiTokens.add(apiToken);

        saveTokensFile();
    }

    public void removeApiToken(ApiToken apiToken) {
        apiTokens.remove(apiToken);

        saveTokensFile();
    }

    public void removeApiTokens(List<ApiToken> apiTokens) {
        this.apiTokens.removeAll(apiTokens);

        saveTokensFile();
    }

    public List<ApiToken> retrieveApiTokens() {
        return new LinkedList<>(apiTokens);
    }

    private void saveTokensFile() {
        try {
            String json = gson.toJson(apiTokens);
            Files.writeString(tokensJsonPath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            gameOver.endIt(logger, e, String.format("Could not save the tokens file."));
        }
    }

}
