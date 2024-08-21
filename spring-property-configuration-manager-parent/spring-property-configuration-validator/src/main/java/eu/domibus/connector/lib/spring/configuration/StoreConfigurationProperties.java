/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration;

import eu.domibus.connector.lib.spring.configuration.validation.CheckResourceIsReadable;
import eu.domibus.connector.lib.spring.configuration.validation.CheckStoreIsLoadable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;


@CheckStoreIsLoadable
public class StoreConfigurationProperties {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StoreConfigurationProperties.class);

    /**
     * Path to the Key/Truststore
     */
    @CheckResourceIsReadable
    private Resource path;

    /**
     * Password to open the Store
     */
    @NotNull
    private String password;

    /**
     * Type of the java key store
     */
    @NotNull
    private String type = "JKS";

    public StoreConfigurationProperties() {
    }

    public StoreConfigurationProperties(Resource path, String password) {
        this.path = path;
        this.password = password;
    }

    public Resource getPath() {
        return path;
    }

    public void setPath(Resource path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPathUrlAsString() {
        try {
            if (path == null) {
                LOGGER.debug("#getPathUrlAsString: resolved to null");
                return null;
            }
            LOGGER.trace("#getPathUrlAsString: get url from [{}] to [{}]", path, path.getURL());
            return path.getURL().toString();
        } catch (IOException e) {
            throw new UncheckedIOException("#getPathUrlAsString: path: [" + path + "]", e);
        }
    }

    public void validatePathReadable() {
        if (getPath() == null) {
            throw new ValidationException("Path is null!");
        }
        try {
            InputStream inputStream = this.getPath().getInputStream();
            if (inputStream == null) {
                throw new ValidationException("Input Stream from path is null!");
            }
            inputStream.close();
        } catch (IOException e) {
            throw new ValidationException("IOException occured during open", e);
        }
    }

    public void validatePathWriteable() {
        if (getPath() == null) {
            throw new ValidationException("Path is null!");
        }
        try {
            if (Files.isWritable(Paths.get(getPath().getURI()))) {

            }
            throw new ValidationException("Path not writeable!");
        } catch (IOException e) {
            throw new ValidationException("IOException occured during open", e);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("IllegalArgumentException occured during open", e);
        }

    }

    public void validateKeyExists(String alias, String password) {
        KeyStore keyStore;
        keyStore = loadKeyStore();

        try {
            Key key = keyStore.getKey(alias, password.toCharArray());
            if (key == null) {
                throw new ValidationException("No key found for alias [%s]");
            }
        } catch (KeyStoreException e) {
            throw new ValidationException(
                    String.format("Key Store exception when retrieving key alias [%s]", alias), e);
        } catch (NoSuchAlgorithmException e) {
            throw new ValidationException(
                    String.format("No such key exception when retrieving key alias [%s]", alias),
                    e);
        } catch (UnrecoverableKeyException e) {
            throw new ValidationException(
                    String.format("Validation exception when retrieving key alias [%s]", alias), e);
        }
    }

    public void validateCertExists(String alias) {
        KeyStore keyStore;
        keyStore = loadKeyStore();
        try {
            Certificate certificate = keyStore.getCertificate(alias);
            if (certificate == null) {
                throw new ValidationException(
                        String.format("No certificate found for alias [%s]", alias));
            }
        } catch (KeyStoreException e) {
            throw new ValidationException(String.format(
                    "Key store exception occured while loading certificate with alias [%s] from key store",
                    alias), e);
        }
    }

    public KeyStore loadKeyStore() {
        try {
            validatePathReadable();
        } catch (ValidationException ve) {
            throw new CannotLoadKeyStoreException(
                    String.format("Cannot load key store from path %s", getPath()), ve);
        }
        if (password == null) {
            password = "";
        }
        char[] pwdArray = password.toCharArray();
        try (InputStream inputStream = getPath().getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance(this.type);
            keyStore.load(inputStream, pwdArray);
            return keyStore;
        } catch (IOException | KeyStoreException | CertificateException |
                 NoSuchAlgorithmException e) {
            throw new CannotLoadKeyStoreException(
                    String.format("Cannot load key store from path %s", getPath()), e);
        }
    }

    public static class CannotLoadKeyStoreException extends RuntimeException {
        public CannotLoadKeyStoreException() {
        }

        public CannotLoadKeyStoreException(String message) {
            super(message);
        }

        public CannotLoadKeyStoreException(String message, Throwable cause) {
            super(message, cause);
        }

        public CannotLoadKeyStoreException(Throwable cause) {
            super(cause);
        }

        public CannotLoadKeyStoreException(String message, Throwable cause,
                                           boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


    public static class ValidationException extends RuntimeException {
        public ValidationException() {
        }

        public ValidationException(String message) {
            super(message);
        }

        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }

        public ValidationException(Throwable cause) {
            super(cause);
        }

        public ValidationException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}
