/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.gw.service;

import static org.assertj.core.api.Assertions.assertThat;

import eu.ecodex.configuration.pmode.Configuration;
import eu.ecodex.utils.monitor.gw.config.GatewayRestInterfaceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("tests needs external resources")
public class PModeDownloaderTest {

    PModeDownloader pModeDownloader;

    @BeforeEach
    public void beforeEach() {
        GatewayRestInterfaceConfiguration gwConfig = new GatewayRestInterfaceConfiguration();
        gwConfig.setUsername("admin");
        gwConfig.setUrl("http://localhost:8020/domibus");
        gwConfig.setPassword("123456");

        pModeDownloader = new PModeDownloader(gwConfig);
    }

    @Test
    void downloadPModes() {
        Configuration configuration = pModeDownloader.downloadPModes();
        assertThat(configuration).isNotNull();
    }

//    @Test
//    void authenticate() {
//    }

}