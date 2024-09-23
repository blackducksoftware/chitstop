/*
 * chitstop
 *
 * Copyright (c) 2024 Black Duck Software, Inc.
 *
 * Use subject to the terms and conditions of the Black Duck Software End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.blackduck.integration.chitstop.exception;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class GameOver {
    public void endIt(Logger logger, Exception e, String message) {
        logger.error(e.getMessage(), e);
        throw new GameOverException(message);
    }

    public void endIt(String message) {
        throw new GameOverException(message);
    }

}
