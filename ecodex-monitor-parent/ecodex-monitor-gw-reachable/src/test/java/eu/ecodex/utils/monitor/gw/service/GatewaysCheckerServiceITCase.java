/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.gw.service;

import static org.assertj.core.api.Assertions.assertThat;

import eu.ecodex.utils.monitor.gw.GatewayMonitorAutoConfiguration;
import eu.ecodex.utils.monitor.gw.domain.AccessPoint;
import eu.ecodex.utils.monitor.gw.dto.AccessPointStatusDTO;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.server.ServerStarter;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {GatewayMonitorAutoConfiguration.class}
)
@ActiveProfiles("test")
public class GatewaysCheckerServiceITCase {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GatewaysCheckerServiceITCase.class);

    @Autowired
    GatewaysCheckerService gatewaysCheckerService;

    @Test
    void getGatewayStatus_serverCrtDoesNotMatchName() {

        ConfigurableApplicationContext SERVER1 = ServerStarter.startServer1();

        AccessPoint ap = new AccessPoint();
        ap.setName("gw1");
        ap.setEndpoint("https://localhost:" + ServerStarter.getServerPort(SERVER1) + "/");

        AccessPointStatusDTO gatewayStatus = gatewaysCheckerService.getGatewayStatus(ap);

        LOGGER.info("Gateway status is: [{}]", gatewayStatus);

        assertThat(gatewayStatus.getFailures()).hasSize(1);
    }

    @Test
    void getGatewayStatus2_illegalClientCrt() {

        ConfigurableApplicationContext SERVER2 = ServerStarter.startServer2();

        AccessPoint ap = new AccessPoint();
        ap.setName("gw2");
        ap.setEndpoint("https://localhost:" + ServerStarter.getServerPort(SERVER2) + "/");

        AccessPointStatusDTO gatewayStatus = gatewaysCheckerService.getGatewayStatus(ap);

        LOGGER.info("Gateway status is: [{}]", gatewayStatus);

        assertThat(gatewayStatus.getFailures()).hasSize(1);
    }

    @Test
    void getGatewayStatus3() {

        ConfigurableApplicationContext SERVER3 = ServerStarter.startServer3();

        AccessPoint ap = new AccessPoint();
        ap.setName("gw3");
        ap.setEndpoint("https://localhost:" + ServerStarter.getServerPort(SERVER3) + "/");

        AccessPointStatusDTO gatewayStatus = gatewaysCheckerService.getGatewayStatus(ap);

        LOGGER.info("Gateway status is: [{}]", gatewayStatus);

        assertThat(gatewayStatus.getFailures()).hasSize(0);
    }

    @Test
    void getGatewayStatus_recheck() throws InterruptedException {

        ConfigurableApplicationContext SERVER3 = ServerStarter.startServer3();

        AccessPoint ap = new AccessPoint();
        ap.setName("gw3");
        ap.setEndpoint("https://localhost:" + ServerStarter.getServerPort(SERVER3) + "/");

        AccessPointStatusDTO gatewayStatus = gatewaysCheckerService.getGatewayStatus(ap);
        LOGGER.info("Gateway status is: [{}]", gatewayStatus);
        assertThat(gatewayStatus.getFailures()).hasSize(0);

        LOGGER.info("sleep 8s");
        Thread.sleep(Duration.ofSeconds(8).toMillis());

        AccessPointStatusDTO gatewayStatus2 = gatewaysCheckerService.getGatewayStatus(ap);
        LOGGER.info("Gateway status is: [{}]", gatewayStatus2);
        assertThat(gatewayStatus).isNotEqualTo(gatewayStatus2);


    }

    @Test
    void getGatewayStatus4_illegalServerCrt() throws InterruptedException {

        ConfigurableApplicationContext SERVER4 = ServerStarter.startServer4();

        AccessPoint ap = new AccessPoint();
        ap.setName("gw4");
        ap.setEndpoint("https://localhost:" + ServerStarter.getServerPort(SERVER4) + "/");

        AccessPointStatusDTO gatewayStatus = gatewaysCheckerService.getGatewayStatus(ap);
        AccessPointStatusDTO gatewayStatus1 = gatewaysCheckerService.getGatewayStatus(ap);
        Thread.sleep(Duration.ofSeconds(2).toMillis());
        AccessPointStatusDTO gatewayStatus2 = gatewaysCheckerService.getGatewayStatus(ap);

        LOGGER.info("Gateway status is: [{}]", gatewayStatus);

        assertThat(gatewayStatus.getFailures()).hasSize(1);

        assertThat(gatewayStatus).isSameAs(gatewayStatus1);
        assertThat(gatewayStatus).isSameAs(gatewayStatus2);
    }


}