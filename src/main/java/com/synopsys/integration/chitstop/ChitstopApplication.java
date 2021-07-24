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
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synopsys.integration.chitstop.exception.GameOverException;
import com.synopsys.integration.chitstop.rest.controller.StringToVmKeyConverter;
import com.synopsys.integration.chitstop.rest.model.VmKey;
import com.synopsys.integration.chitstop.rest.model.VmKeyTypeAdapter;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.LatestVersionFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.MavenLatestVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.NugetLatestVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.PrefixSuffixLatestVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.SemverSupport;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.Slf4jIntLogger;
import com.synopsys.integration.rest.client.IntHttpClient;
import com.synopsys.integration.rest.proxy.ProxyInfo;

import springfox.documentation.spring.web.json.Json;

@SpringBootApplication
public class ChitstopApplication implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(ChitstopApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(ChitstopApplication.class, args);
        } catch (GameOverException e) {
            logger.error("An unrecoverable error occurred.", e);
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToVmKeyConverter());
    }

    @Bean
    public WatchService watchService() throws IOException {
        return FileSystems.getDefault().newWatchService();
    }

    @Bean
    public IntLogger logger() {
        return new Slf4jIntLogger(ChitstopApplication.logger);
    }

    @Bean
    public IntHttpClient httpClient() {
        return new IntHttpClient(logger(), gson(), 120, false, ProxyInfo.NO_PROXY_INFO);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                   .setPrettyPrinting()
                   .disableHtmlEscaping()
                   .registerTypeAdapter(VmKey.class, new VmKeyTypeAdapter())
                   .registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter())
                   .create();
    }

    @Bean
    public ArtifactoryClient artifactoryClient() {
        return new ArtifactoryClient(logger(), httpClient(), gson());
    }

    @Bean
    /*
    ejk - at the moment, we only need one thread for the ApiTokenWatcher, if we
    need more, we'll need to use a different ExecutorService implementation
     */
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public SemverSupport semverSupport() {
        return new SemverSupport();
    }

    @Bean
    public MavenLatestVersionFilter mavenLatestVersionFilter() {
        return new MavenLatestVersionFilter(semverSupport());
    }

    @Bean
    public NugetLatestVersionFilter nugetLatestVersionFilter() {
        return new NugetLatestVersionFilter(semverSupport());
    }

    @Bean
    public LatestVersionFinder detectFontBundleLatestVersionFinder() {
        return new LatestVersionFinder(artifactoryClient(), new PrefixSuffixLatestVersionFilter(semverSupport(), "risk-report-fonts-", ".zip"));
    }

    @Bean
    public LatestVersionFinder mavenLatestVersionFinder() {
        return new LatestVersionFinder(artifactoryClient(), mavenLatestVersionFilter());
    }

    @Bean
    public LatestVersionFinder nugetLatestVersionFinder() {
        return new LatestVersionFinder(artifactoryClient(), nugetLatestVersionFilter());
    }

}
