/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.service.vms;

import com.blackduck.integration.chitstop.rest.model.BlackDuckVmStatus;
import com.synopsys.integration.blackduck.api.core.BlackDuckPath;
import com.synopsys.integration.blackduck.api.generated.discovery.ApiDiscovery;
import com.synopsys.integration.blackduck.api.generated.response.HealthChecksLivenessView;
import com.synopsys.integration.blackduck.service.BlackDuckApiClient;
import com.synopsys.integration.blackduck.service.dataservice.BlackDuckRegistrationService;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

public class BlackDuckStatusService {
    // liveness and readiness are 'currently' identical in response payload
    public static final BlackDuckPath<HealthChecksLivenessView> LIVENESS = BlackDuckPath.single("/api/health-checks/liveness", HealthChecksLivenessView.class);
    public static final BlackDuckPath<HealthChecksLivenessView> READINESS = BlackDuckPath.single("/api/health-checks/readiness", HealthChecksLivenessView.class);

    private final HttpUrl blackDuckUrl;
    private final ApiDiscovery apiDiscovery;
    private final BlackDuckApiClient blackDuckApiClient;
    private final BlackDuckRegistrationService blackDuckRegistrationService;

    public BlackDuckStatusService(HttpUrl blackDuckUrl, ApiDiscovery apiDiscovery, BlackDuckApiClient blackDuckApiClient, BlackDuckRegistrationService blackDuckRegistrationService) {
        this.blackDuckUrl = blackDuckUrl;
        this.apiDiscovery = apiDiscovery;
        this.blackDuckApiClient = blackDuckApiClient;
        this.blackDuckRegistrationService = blackDuckRegistrationService;
    }

    public BlackDuckVmStatus retrieveStatus() throws IntegrationException {
        HealthChecksLivenessView liveness = blackDuckApiClient.getResponse(apiDiscovery.metaSingleResponse(LIVENESS));
        HealthChecksLivenessView readiness = blackDuckApiClient.getResponse(apiDiscovery.metaSingleResponse(READINESS));

        String version = blackDuckRegistrationService.getBlackDuckServerData().getVersion();

        return new BlackDuckVmStatus(blackDuckUrl.string(), version, readiness.getHealthy(), liveness.getHealthy());
    }

}
