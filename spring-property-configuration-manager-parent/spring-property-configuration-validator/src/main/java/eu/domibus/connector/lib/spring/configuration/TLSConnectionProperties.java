/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.lib.spring.configuration;

import java.util.List;
import javax.net.ssl.SSLContext;

public class TLSConnectionProperties extends KeyAndKeyStoreAndTrustStoreConfigurationProperties {

    /**
     * Minimum TLS version for this
     * connection eg. TLSv1, TLSv1.1, TLSv1.2, TLSv1.3
     * must be supported by java:
     * also see {@link SSLContext}
     */
    private String minTls;

    /**
     * A list of TLS proxy servers
     * note: currently only the FIRST
     * proxy server will be used!
     */
    private List<String> proxy;

    public String getMinTls() {
        return minTls;
    }

    public void setMinTls(String minTls) {
        this.minTls = minTls;
    }

    public List<String> getProxy() {
        return proxy;
    }

    public void setProxy(List<String> proxy) {
        this.proxy = proxy;
    }
}
