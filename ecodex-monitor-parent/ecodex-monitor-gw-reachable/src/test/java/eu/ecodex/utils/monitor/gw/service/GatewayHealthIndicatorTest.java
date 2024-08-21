/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.gw.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import eu.ecodex.utils.monitor.app.MonitorAppConfiguration;
import eu.ecodex.utils.monitor.gw.GatewayMonitorAutoConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {GatewayMonitorAutoConfiguration.class, MonitorAppConfiguration.class}
)
@ActiveProfiles("test")
@Disabled("test needs external resource!")
public class GatewayHealthIndicatorTest {

    @Autowired
    GatewayHealthIndicator gatewayHealthIndicator;

    @LocalServerPort
    int localPort;


    @Test
    public void testGateway() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate build = restTemplateBuilder
                .uriTemplateHandler(new RootUriTemplateHandler(
                        "http://localhost:" + localPort + "/actuator/health"))
                .basicAuthentication("test", "test")
                .build();

        ResponseEntity<String> forEntity = build.getForEntity("/", String.class);

        assertThat(forEntity).isNotNull();

        System.out.println(forEntity.getBody());
    }


}