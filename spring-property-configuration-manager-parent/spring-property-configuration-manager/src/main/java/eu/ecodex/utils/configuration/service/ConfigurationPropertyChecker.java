/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.service;

import java.util.Collection;
import java.util.List;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;

public interface ConfigurationPropertyChecker {

    /**
     * @param configurationPropertySource - the property source which provides the properties to check
     * @param basePackageFilter           - the filter under which all with with @see {@link org.springframework.boot.context.properties.ConfigurationProperties}
     *                                    annotated Properties are bound and checked within this binding
     * @return - a list of validation errors
     * @throws org.springframework.boot.context.properties.bind.BindException in case of an failure during binding
     */
    List<ValidationErrors> validateConfiguration(
            ConfigurationPropertySource configurationPropertySource, String... basePackageFilter);

    List<ValidationErrors> validateConfiguration(
            ConfigurationPropertySource configurationPropertySource,
            Collection<Class> configurationClasses);

}
