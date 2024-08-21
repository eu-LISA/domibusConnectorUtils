/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyConfigurationProperties;
import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.springframework.core.io.ClassPathResource;

public class ConstraintViolationSetHelper {

    public static <T> void printSet(Set<ConstraintViolation<T>> constraintViolationSet) {
        constraintViolationSet.stream().forEach(c -> System.out.println(
                "propertyPath: " + c.getPropertyPath() + " msg: " + c.getMessage()));
    }


    public static StoreConfigurationProperties generateTestStore() {
        StoreConfigurationProperties storeConfigurationProperties =
                new StoreConfigurationProperties();
        storeConfigurationProperties.setPath(new ClassPathResource("keystores/client-bob.jks"));
        storeConfigurationProperties.setPassword("12345");
        return storeConfigurationProperties;
    }

    public static KeyConfigurationProperties generateTestKeyConfig() {
        KeyConfigurationProperties keyConfigurationProperties = new KeyConfigurationProperties();
        keyConfigurationProperties.setAlias("bob");
        keyConfigurationProperties.setPassword("");
        return keyConfigurationProperties;
    }
}
