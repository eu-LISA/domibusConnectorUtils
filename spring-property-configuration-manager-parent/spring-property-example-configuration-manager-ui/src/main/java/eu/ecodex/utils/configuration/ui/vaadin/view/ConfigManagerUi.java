/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.ui.vaadin.view;

import eu.ecodex.configuration.spring.EnablePropertyConfigurationManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication(scanBasePackages = {"eu.ecodex.utils.configuration.ui",
        "eu.ecodex.utils.configuration.example1"})
@EnablePropertyConfigurationManager
public class ConfigManagerUi {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(ConfigManagerUi.class);

        builder.run(args);
    }

}
