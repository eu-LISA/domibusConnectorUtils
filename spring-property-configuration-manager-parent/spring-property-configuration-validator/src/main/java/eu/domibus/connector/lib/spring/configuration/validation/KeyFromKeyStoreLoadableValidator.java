/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.KeyAndKeyStoreConfigurationProperties;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class KeyFromKeyStoreLoadableValidator implements
        ConstraintValidator<CheckKeyIsLoadableFromKeyStore, KeyAndKeyStoreConfigurationProperties> {

    private Validator validator;

    @Override
    public void initialize(CheckKeyIsLoadableFromKeyStore constraintAnnotation) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public boolean isValid(KeyAndKeyStoreConfigurationProperties value,
                           ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> validations =
                new HashSet<>();
        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> path =
                validator.validateProperty(value, "privateKey");
        validations.addAll(path);
        Set<ConstraintViolation<KeyAndKeyStoreConfigurationProperties>> keyStoreValidation =
                validator.validateProperty(value, "keyStore");
        validations.addAll(keyStoreValidation);
        if (!validations.isEmpty()) {
            return false;
        }

        context.disableDefaultConstraintViolation();

        return HelperMethods.checkKeyIsLoadable(context, value.getKeyStore(),
                value.getPrivateKey());
    }


}
