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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
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
    public static final Function<ApiToken, Predicate<ApiToken>> MATCHING_TOKEN = (thisApiToken) -> (thatApiToken) -> {
        if (thisApiToken.getVmKey().equals(thatApiToken.getVmKey())) {
            if (thisApiToken.getToken().equals(thatApiToken.getToken())) {
                return true;
            }

            boolean[] usernameAndName = new boolean[] {
                thisApiToken.getUsername().equals(thatApiToken.getUsername()),
                thisApiToken.getTokenName().equals(thatApiToken.getTokenName())
            };
            if (BooleanUtils.and(usernameAndName)) {
                return true;
            }
        }
        return false;
    };

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

    public void storeApiToken(ApiToken apiToken) {
        findExistingToken(apiToken)
            .ifPresentOrElse(
                (existing) -> updateToken(existing, apiToken),
                () -> insertToken(apiToken)
            );

        try {
            String json = objectMapper.writeValueAsString(apiTokens);
            Files.writeString(tokensJsonPath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            gameOver.endIt(logger, e, String.format("Could not store the api token: %s", apiToken));
        }
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

    private Optional<ApiToken> findExistingToken(ApiToken apiToken) {
        return apiTokens
                   .stream()
                   .filter(MATCHING_TOKEN.apply(apiToken))
                   .findFirst();
    }

    private void insertToken(ApiToken apiToken) {
        apiTokens.add(apiToken);
    }

    private void updateToken(ApiToken apiToken, ApiToken newValues) {
        setProperty(apiToken::setToken, newValues::getToken);
        setProperty(apiToken::setTokenName, newValues::getTokenName);
        setProperty(apiToken::setDescription, newValues::getDescription);
        setProperty(apiToken::setScope, newValues::getScope);
    }

    private <T extends Object> void setProperty(Consumer<T> setter, Supplier<T> getter) {
        T newValue = getter.get();
        if (ObjectUtils.isNotEmpty(newValue)) {
            setter.accept(newValue);
        }
    }

}
