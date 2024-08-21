/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration;

import eu.domibus.connector.lib.spring.configuration.validation.CheckKeyIsLoadableFromKeyStore;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


@CheckKeyIsLoadableFromKeyStore
public class KeyAndKeyStoreAndTrustStoreConfigurationProperties
        extends KeyAndKeyStoreConfigurationProperties {


    /**
     * Configuration of the TrustStore
     */
    @NestedConfigurationProperty
    @Valid
    @NotNull
    private StoreConfigurationProperties trustStore;


    public KeyAndKeyStoreAndTrustStoreConfigurationProperties() {
    }

    public StoreConfigurationProperties getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(StoreConfigurationProperties trustStore) {
        this.trustStore = trustStore;
    }
}
