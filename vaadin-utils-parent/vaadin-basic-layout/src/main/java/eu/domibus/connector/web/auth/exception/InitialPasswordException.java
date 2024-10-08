/*
 * Copyright 2024 European Union Agency for the Operational Management of Large-Scale IT Systems
 * in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.web.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown when an initial (default) password is being used for authentication.
 * It indicates that the user needs to change their password.
 */
public class InitialPasswordException extends AuthenticationException {
    public InitialPasswordException(String explanation) {
        super(explanation);
    }

    public InitialPasswordException(String msg, Throwable t) {
        super(msg, t);
    }
}
