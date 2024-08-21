/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration.validation;

import static org.assertj.core.api.Assertions.assertThat;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;


public class StoreLoadableValidatorTest {


    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void isValid() throws Exception {
        StoreConfigurationProperties storeConfigurationProperties =
                new StoreConfigurationProperties();

        storeConfigurationProperties.setPassword("12345");
        storeConfigurationProperties.setPath(
                new ClassPathResource("keystores/connector-backend.jks"));

        Set<ConstraintViolation<StoreConfigurationProperties>> validate =
                validator.validate(storeConfigurationProperties);

        assertThat(validate).hasSize(0);
    }


    @Test
    public void isValid_wrongPassword_shouldNotBeValid() {
        StoreConfigurationProperties storeConfigurationProperties =
                new StoreConfigurationProperties();

        storeConfigurationProperties.setPassword("WRONG");
        storeConfigurationProperties.setPath(
                new ClassPathResource("keystores/connector-backend.jks"));

        Set<ConstraintViolation<StoreConfigurationProperties>> validate =
                validator.validate(storeConfigurationProperties);

        validate.stream().forEach(c -> System.out.println(c.getMessage()));

        assertThat(validate).hasSize(2);
    }

    @Test
    public void isValid_wrongPATH_shouldNotBeValid() {
        StoreConfigurationProperties storeConfigurationProperties =
                new StoreConfigurationProperties();

        storeConfigurationProperties.setPassword("12345");
        storeConfigurationProperties.setPath(
                new ClassPathResource("keystores/NONEXISTANT_KEYSTORE.jks"));

        Set<ConstraintViolation<StoreConfigurationProperties>> validate =
                validator.validate(storeConfigurationProperties);
        validate.stream().forEach(c -> System.out.println(
                "propertyPath: " + c.getPropertyPath() + " msg: " + c.getMessage()));

        assertThat(validate).hasSize(2);
    }


}