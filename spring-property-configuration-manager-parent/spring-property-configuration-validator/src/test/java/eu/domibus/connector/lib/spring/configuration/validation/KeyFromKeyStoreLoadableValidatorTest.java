/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration.validation;

import static eu.domibus.connector.lib.spring.configuration.validation.ConstraintViolationSetHelper.printSet;
import static org.assertj.core.api.Assertions.assertThat;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;


public class KeyFromKeyStoreLoadableValidatorTest {

    private static Validator validator;

    private KeyAndKeyStoreConfigurationProperties props;

    @BeforeAll
    public static void beforeClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @BeforeEach
    public void setUp() {
        props = new KeyAndKeyStoreConfigurationProperties();
        props.setKeyStore(ConstraintViolationSetHelper.generateTestStore());
        props.setPrivateKey(ConstraintViolationSetHelper.generateTestKeyConfig());
    }


    @Test
    public void isValid() throws Exception {
        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
                validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(0);
    }

    @Test
    public void isValid_wrongStorePath_shouldNotValidate() {
        props.getKeyStore().setPath(new ClassPathResource("/does/not/exist"));

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
                validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(3);
    }


    @Test
    public void isValid_wrongKeyAlias_shouldNotValidate() {
        props.getPrivateKey().setAlias("WRONG_ALIAS");

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
                validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }

    @Test
    public void isValid_wrongKeyPassword_shouldNotValidate() {
        props.getPrivateKey().setPassword("WRONG_PASSWORD");

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
                validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(1);
    }


    @Test
    public void isValid_keyInformationIsNull() {
        props.setPrivateKey(null);

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
                validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(2);
    }

    @Test
    public void isValid_keyStoreInformationIsNull() {
        props.setKeyStore(null);

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validate =
                validator.validate(props);
        printSet(validate);
        assertThat(validate).hasSize(2);
    }


}