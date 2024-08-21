/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration.validation;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoreLoadableValidator
        implements ConstraintValidator<CheckStoreIsLoadable, StoreConfigurationProperties> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreLoadableValidator.class);

    private Validator validator;

    @Override
    public void initialize(CheckStoreIsLoadable constraintAnnotation) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public boolean isValid(StoreConfigurationProperties value, ConstraintValidatorContext context) {
        try {
            if (value == null) {
                return true;
            }
            Set<ConstraintViolation<StoreConfigurationProperties>> path =
                    validator.validateProperty(value, "path");
            if (!path.isEmpty()) {
                return false;
            }
            try {
                value.loadKeyStore();
            } catch (StoreConfigurationProperties.CannotLoadKeyStoreException exception) {
                //TODO: nice message!
                context.buildConstraintViolationWithTemplate(exception.getCause().getMessage())
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("exception occured", e);
            //throw new RuntimeException(e);
//            context.buildConstraintViolationWithTemplate(e.getCause().getMessage()).addConstraintViolation();
            return false;
        }


//        private KeyStore loadKeyStore() {
//            validatePathReadable();
//            char[] pwdArray = password.toCharArray();
//            try (InputStream inputStream = getPath().getInputStream()) {
//                KeyStore keyStore = KeyStore.getInstance("JKS");
//                keyStore.load(inputStream, pwdArray);
//                return keyStore;
//            } catch (KeyStoreException e) {
//                throw new StoreConfigurationProperties.ValidationException("KeyStoreException occured during open keyStore", e);
//            } catch (IOException e) {
//                throw new StoreConfigurationProperties.ValidationException("IOException occured during open", e);
//            } catch (CertificateException | NoSuchAlgorithmException e) {
//                throw new StoreConfigurationProperties.ValidationException("Exception occured during open keyStore", e);
//            }
//        }


        return true;
    }
}
