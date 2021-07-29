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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synopsys.integration.chitstop.exception.GameOverException;
import com.synopsys.integration.chitstop.rest.controller.StringToVmKeyConverter;
import com.synopsys.integration.chitstop.rest.model.VmKey;
import com.synopsys.integration.chitstop.rest.model.VmKeyTypeAdapter;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProducts;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryProductsFactory;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.NestedArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.artifactfinder.RepoArtifactFinder;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.MavenVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.NugetVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.PrefixSuffixVersionFilter;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.SemverSupport;
import com.synopsys.integration.chitstop.service.artifactory.versionfinder.VersionFinder;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.Slf4jIntLogger;
import com.synopsys.integration.rest.HttpUrl;
import com.synopsys.integration.rest.client.IntHttpClient;
import com.synopsys.integration.rest.proxy.ProxyInfo;

import springfox.documentation.spring.web.json.Json;

@SpringBootApplication
public class ChitstopApplication implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(ChitstopApplication.class);
    private static final String ALPHANUMERIC_WITH_HYPHEN = "[\\w\\-]+";

    public static void main(String[] args) {
        try {
            SpringApplication.run(ChitstopApplication.class, args);
        } catch (GameOverException e) {
            logger.error("An unrecoverable error occurred.", e);
        }
    }

    @Value("${read.only.artifactory.url}")
    private String readOnlyArtifactoryUrl;

    @Value("${write.artifactory.url}")
    private String writeArtifactoryUrl;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToVmKeyConverter());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // the root needs to go to index.html
        registry.addViewController("/").setViewName("forward:/index.html");

        // anything that looks like:
        // /alphaNumericWordWithHyphen
        // which should go to index.html since all /api endpoints will have more
        // than a single level
        registry.addViewController(String.format("/{onlyNeededToEnableRegex:%s}", ALPHANUMERIC_WITH_HYPHEN)).setViewName("forward:/index.html");

        // anything that looks like:
        // /alphaNumericWordWithHyphen/alphaNumericWordWithHyphen/...
        // needs to go to index.html
        // but anything that starts with:
        // /api/whocares...
        // needs to NOT go to index.html
        registry.addViewController(String.format("/{onlyNeededToEnableRegex:^(?!api$).*$}/**/{onlyNeededToEnableRegex2:%s}", ALPHANUMERIC_WITH_HYPHEN)).setViewName("forward:/index.html");
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
    public ArtifactoryClient artifactoryClient() throws IntegrationException {
        HttpUrl readOnlyArtifactory = new HttpUrl(readOnlyArtifactoryUrl);
        HttpUrl writeArtifactory = new HttpUrl(writeArtifactoryUrl);
        return new ArtifactoryClient(logger(), httpClient(), gson(), readOnlyArtifactory, writeArtifactory);
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
    public MavenVersionFilter mavenLatestVersionFilter() {
        return new MavenVersionFilter(semverSupport());
    }

    @Bean
    public NugetVersionFilter nugetLatestVersionFilter() {
        return new NugetVersionFilter(semverSupport());
    }

    @Bean
    public VersionFinder detectFontBundleLatestVersionFinder() throws IntegrationException {
        return new VersionFinder(artifactoryClient(), new PrefixSuffixVersionFilter(semverSupport(), "risk-report-fonts-", ".zip"));
    }

    @Bean
    public VersionFinder mavenLatestVersionFinder() throws IntegrationException {
        return new VersionFinder(artifactoryClient(), mavenLatestVersionFilter());
    }

    @Bean
    public VersionFinder nugetLatestVersionFinder() throws IntegrationException {
        return new VersionFinder(artifactoryClient(), nugetLatestVersionFilter());
    }

    @Bean
    public RepoArtifactFinder repoArtifactFinder() throws IntegrationException {
        return new RepoArtifactFinder(artifactoryClient());
    }

    @Bean
    public NestedArtifactFinder nestedArtifactFinder() throws IntegrationException {
        return new NestedArtifactFinder(artifactoryClient());
    }

    @Bean
    public ArtifactoryProductsFactory artifactoryProductsFactory() throws IntegrationException {
        return new ArtifactoryProductsFactory(detectFontBundleLatestVersionFinder(), mavenLatestVersionFinder(), nugetLatestVersionFinder(), repoArtifactFinder(), nestedArtifactFinder());
    }

    @Bean
    public ArtifactoryProducts artifactoryProducts() throws IntegrationException {
        return artifactoryProductsFactory().createDefault();
    }

}
