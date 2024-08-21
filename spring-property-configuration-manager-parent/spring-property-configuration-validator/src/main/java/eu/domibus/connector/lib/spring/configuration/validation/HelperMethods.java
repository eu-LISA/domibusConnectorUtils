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
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelperMethods.class);

    public static boolean checkKeyIsLoadable(ConstraintValidatorContext context,
                                             StoreConfigurationProperties storeConfig,
                                             KeyConfigurationProperties keyConfig) {
        if (keyConfig == null || keyConfig.getAlias() == null || storeConfig ==
                null) { //DO NOT CHECK IF keyConfig or keyAlias or storeConfig is null
            LOGGER.trace(
                    "checkKeyIsLoadable skipped because either keyConfig, keyConfig.alias or storeConfig is null!");
            return true;
        }
        LOGGER.trace("checkKeyIsLoadable with context [{}], storeConfig [{}], keyConfig [{}]",
                context, storeConfig, keyConfig);
        String alias = keyConfig.getAlias();
        String password = keyConfig.getPassword();
        String logPassword = "** enableDebugToSee **";
        if (LOGGER.isDebugEnabled()) {
            logPassword = password;
        }

        char[] passwordArray = password.toCharArray();


        KeyStore keyStore;
        try {
            keyStore = storeConfig.loadKeyStore();
        } catch (Exception e) {
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addConstraintViolation(); //TODO: add PropertyNode
            return false;
        }


        try {
            if (!keyStore.containsAlias(alias)) {
                String error = String.format("key alias [%s] does not exist in key store!", alias);

                context.buildConstraintViolationWithTemplate(error)
                        .addPropertyNode("privateKey")
                        .addPropertyNode("alias")
                        .addConstraintViolation();
                return false;
            }

            Key key = keyStore.getKey(alias, passwordArray);
            if (key != null) {
                return true;
            } else {
                String error =
                        String.format("Cannot retrieve key with alias [%s] and password [%s]! ",
                                alias, logPassword);
                context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            String error =
                    String.format("key with alias [%s] could not recovered! KeyStoreException!",
                            alias, logPassword);
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            String error = String.format(
                    "key with alias [%s] could not recovered! No such algorithm exception", alias,
                    logPassword);
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        } catch (UnrecoverableKeyException e) {
            String error = String.format(
                    "key with alias [%s] could not recovered! Check if the password [%s] is correct",
                    alias, logPassword);
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }
        return false;
    }

}
