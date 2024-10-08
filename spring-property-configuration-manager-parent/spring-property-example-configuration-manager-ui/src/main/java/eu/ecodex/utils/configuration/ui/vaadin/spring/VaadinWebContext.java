/*
 * Copyright 2024 European Union Agency for the Operational Management of Large-Scale IT Systems
 * in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.ui.vaadin.spring;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration class for setting up the Vaadin web context.
 *
 * <p>This class is responsible for enabling Vaadin support in the Spring application context and
 * configuring necessary web MVC settings.
 */
@Configuration
@EnableWebMvc
@EnableVaadin("eu.ecodex.utils.configuration.ui.vaadin")
@MultipartConfig
public class VaadinWebContext {
}
