/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


public abstract class CxfCertKeyProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(CxfCertKeyProperties.class);

    /**
     * This property configures the the path to the security policy which should be used for the
     * backend webservice
     * <p>
     * the default security policy requires signed and encrypted messages (body+header)
     * the signing and wss is done with certificates
     */
    @NestedConfigurationProperty
    private Resource wsPolicy = new ClassPathResource("/wsdl/backend.policy.xml");

    /**
     * Configuration of the privateKey store which is used to sign the transferred soap-messages and
     * decrypt the from the backendClient received messages
     */
    @NestedConfigurationProperty
    private KeyAndKeyStoreConfigurationProperties privateKey;

    /**
     * Trust store which is used to verify the from the backendClient signed messages
     * and encrypt the messages transmitted to the backendClients
     */
    @NestedConfigurationProperty
    private CertAndStoreConfigurationProperties trust;

//    public String getBackendPublishAddress() {
//        return backendPublishAddress;
//    }
//
//    public void setBackendPublishAddress(String backendPublishAddress) {
//        this.backendPublishAddress = backendPublishAddress;
//    }

    public KeyAndKeyStoreConfigurationProperties getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(KeyAndKeyStoreConfigurationProperties privateKey) {
        this.privateKey = privateKey;
    }

    public CertAndStoreConfigurationProperties getTrust() {
        return trust;
    }

    public void setTrust(CertAndStoreConfigurationProperties trust) {
        this.trust = trust;
    }

    public Resource getWsPolicy() {
        return wsPolicy;
    }

    public void setWsPolicy(Resource wsPolicy) {
        this.wsPolicy = wsPolicy;
    }

    public Properties getWssProperties() {
        Properties p = mapCertAndStoreConfigPropertiesToMerlinProperties();
        LOGGER.debug("getSignatureProperties() are: [{}]", p);
        return p;
    }

    /**
     * Maps the own configured properties to the crypto Properties
     * also see https://ws.apache.org/wss4j/config.html
     *
     * @return the wss Properties
     */
    public Properties mapCertAndStoreConfigPropertiesToMerlinProperties() {
        Properties p = new Properties();
        p.setProperty("org.apache.wss4j.crypto.provider", "org.apache.wss4j.common.crypto.Merlin");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.type", "jks");
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.password",
                this.getPrivateKey().getStore().getPassword());
        LOGGER.debug("setting [org.apache.wss4j.crypto.merlin.keystore.file={}]",
                this.getPrivateKey().getStore().getPath());
        try {
            p.setProperty("org.apache.wss4j.crypto.merlin.keystore.file",
                    this.getPrivateKey().getStore().getPathUrlAsString());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error with property: [" + getPrefix() + ".privateKey.store.path]\n" +
                            "value is [" + this.getPrivateKey().getStore().getPath() + "]");
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.alias",
                this.getPrivateKey().getKey().getAlias());
        p.setProperty("org.apache.wss4j.crypto.merlin.keystore.private.password",
                this.getPrivateKey().getKey().getPassword());
        p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password",
                this.getTrust().getStore().getPassword());
        try {
            LOGGER.debug("setting [org.apache.wss4j.crypto.merlin.truststore.file={}]",
                    this.getTrust().getStore().getPath());
            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file",
                    this.getTrust().getStore().getPathUrlAsString());
        } catch (Exception e) {
            LOGGER.info("Trust Store Property: [" + getPrefix() + ".trust.store.path]" +
                            "\n cannot be processed. Using the configured privateKey store [{}] as trust store",
                    p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));

            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.file",
                    p.getProperty("org.apache.wss4j.crypto.merlin.keystore.file"));
            p.setProperty("org.apache.wss4j.crypto.merlin.truststore.password",
                    p.getProperty("org.apache.wss4j.crypto.merlin.keystore.password"));
        }
        p.setProperty("org.apache.wss4j.crypto.merlin.load.cacerts",
                Boolean.toString(this.getTrust().isLoadCaCerts()));

        return p;
    }

    private String getPrefix() {
        return "?not set?";
    }


    public static class KeyAndKeyStoreConfigurationProperties {
        /**
         * Configuration of the (Key/Certificate)Store
         */
        @NestedConfigurationProperty
        private StoreConfigurationProperties store;
        /**
         * Configures the default alias to use
         */
        @NestedConfigurationProperty
        private KeyConfigurationProperties key;

        public KeyAndKeyStoreConfigurationProperties() {
        }

        public KeyAndKeyStoreConfigurationProperties(StoreConfigurationProperties keyStore,
                                                     KeyConfigurationProperties key) {
            this.store = keyStore;
            this.key = key;
        }

        public StoreConfigurationProperties getStore() {
            return store;
        }

        public void setStore(StoreConfigurationProperties store) {
            this.store = store;
        }

        public KeyConfigurationProperties getKey() {
            return key;
        }

        public void setKey(KeyConfigurationProperties key) {
            this.key = key;
        }

    }


    public static class CertAndStoreConfigurationProperties {

        /**
         * Configuration of the (Key/Certificate)Store
         */
        @NestedConfigurationProperty
        private StoreConfigurationProperties store;
        /**
         * Load system Ca Certs? (default false).
         * <p>
         * Whether or not to load the CA certs in ${java.home}/lib/security/cacerts (default is false)
         */
        private boolean loadCaCerts = false;

        public CertAndStoreConfigurationProperties() {
        }

        public CertAndStoreConfigurationProperties(StoreConfigurationProperties keyStore) {
            this.store = keyStore;
        }

        public StoreConfigurationProperties getStore() {
            return store;
        }

        public void setStore(StoreConfigurationProperties store) {
            this.store = store;
        }

        public boolean isLoadCaCerts() {
            return loadCaCerts;
        }

        public void setLoadCaCerts(boolean loadCaCerts) {
            this.loadCaCerts = loadCaCerts;
        }
    }

}
