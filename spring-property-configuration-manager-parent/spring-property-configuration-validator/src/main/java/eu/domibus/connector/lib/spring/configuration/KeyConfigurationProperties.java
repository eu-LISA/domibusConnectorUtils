/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * Configuration properties for referencing a
 * key in a key store
 * a alias and a optional password
 */
public class KeyConfigurationProperties {

    /**
     * The alias of the Certificate/Key
     */
    @NotNull(message = "an alias must be provided!")
    @Length(min = 1, message = "Alias must have at least one character!")
    String alias;
    /**
     * The password of the Certificate/Key
     */
    @NotNull
    String password = "";

    public KeyConfigurationProperties() {
    }

    public KeyConfigurationProperties(String alias, String password) {
        this.alias = alias;
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
