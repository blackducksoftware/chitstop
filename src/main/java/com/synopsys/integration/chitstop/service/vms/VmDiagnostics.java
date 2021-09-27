/*
 * chitstop
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.chitstop.service.vms;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.synopsys.integration.blackduck.api.generated.discovery.ApiDiscovery;
import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfig;
import com.synopsys.integration.blackduck.service.BlackDuckApiClient;
import com.synopsys.integration.blackduck.service.BlackDuckServicesFactory;
import com.synopsys.integration.blackduck.service.dataservice.BlackDuckRegistrationService;
import com.synopsys.integration.chitstop.rest.model.BlackDuckVmStatus;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.log.Slf4jIntLogger;

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
