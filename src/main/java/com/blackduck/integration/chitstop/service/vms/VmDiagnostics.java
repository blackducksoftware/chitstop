/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.vms;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blackduck.integration.blackduck.api.generated.discovery.ApiDiscovery;
import com.blackduck.integration.blackduck.configuration.BlackDuckServerConfig;
import com.blackduck.integration.blackduck.service.BlackDuckApiClient;
import com.blackduck.integration.blackduck.service.BlackDuckServicesFactory;
import com.blackduck.integration.blackduck.service.dataservice.BlackDuckRegistrationService;
import com.blackduck.integration.chitstop.rest.model.BlackDuckVmStatus;
import com.blackduck.integration.exception.IntegrationException;
import com.blackduck.integration.log.IntLogger;
import com.blackduck.integration.log.Slf4jIntLogger;

@Service
public class VmDiagnostics {
    private final Logger logger = LoggerFactory.getLogger(VmDiagnostics.class);

    private final BlackDuckVmService blackDuckVmService;
    private final VmStatusCache vmStatusCache;

    @Autowired
    public VmDiagnostics(BlackDuckVmService blackDuckVmService, VmStatusCache vmStatusCache) {
        this.blackDuckVmService = blackDuckVmService;
        this.vmStatusCache = vmStatusCache;
    }

    public List<BlackDuckVmStatus> findAllStatuses(boolean forceRefreshCache) {
        if (vmStatusCache.isEmpty() || forceRefreshCache) {
            refreshCache();
        }

        return vmStatusCache.getStatuses();
    }

    @Scheduled(cron = "0 0/15 * * * *")
    public void refreshCache() {
        logger.debug("refreshing vm status cache");
        List<BlackDuckServerConfig> blackDuckServerConfigs;
        try {
            blackDuckServerConfigs = blackDuckVmService.findAllBlackDuckServerConfigs();
        } catch (IntegrationException e) {
            logger.error("Can't find server configs.", e);
            return;
        }

        IntLogger slf4jLogger = new Slf4jIntLogger(logger);
        List<BlackDuckVmStatus> statuses = new LinkedList<>();
        for (BlackDuckServerConfig blackDuckServerConfig : blackDuckServerConfigs) {
            BlackDuckServicesFactory blackDuckServicesFactory = blackDuckServerConfig.createBlackDuckServicesFactory(slf4jLogger);
            ApiDiscovery apiDiscovery = blackDuckServicesFactory.getApiDiscovery();
            BlackDuckApiClient blackDuckApiClient = blackDuckServicesFactory.getBlackDuckApiClient();
            BlackDuckRegistrationService blackDuckRegistrationService = blackDuckServicesFactory.createBlackDuckRegistrationService();

            BlackDuckStatusService blackDuckStatusService = new BlackDuckStatusService(blackDuckServerConfig.getBlackDuckUrl(), apiDiscovery, blackDuckApiClient, blackDuckRegistrationService);
            try {
                BlackDuckVmStatus status = blackDuckStatusService.retrieveStatus();
                statuses.add(status);
            } catch (IntegrationException e) {
                logger.error(String.format("Couldn't get status for %s", blackDuckServerConfig.getBlackDuckUrl()), e);
            }
        }

        vmStatusCache.setStatuses(statuses);
        logger.debug("refreshed vm status cache");
    }

}
