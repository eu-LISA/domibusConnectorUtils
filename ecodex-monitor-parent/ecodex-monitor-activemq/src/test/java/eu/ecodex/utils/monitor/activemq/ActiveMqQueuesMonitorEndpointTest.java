/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.activemq;


import static org.assertj.core.api.Assertions.assertThat;

import eu.ecodex.utils.monitor.activemq.dto.DestinationInfo;
import eu.ecodex.utils.monitor.activemq.service.ActiveMqQueuesMonitorEndpoint;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.activemq.broker.BrokerRegistry;
import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "dev"})
@EnableJms
class ActiveMqQueuesMonitorEndpointTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";

    @LocalServerPort
    int localServerPort;

    RestTemplate restTemplate;

    @BeforeAll
    public static void beforeAll() throws Exception {
        BrokerRegistry reg = BrokerRegistry.getInstance();

        BrokerService broker = new BrokerService();
        reg.bind("broker", broker);

        broker.addConnector("vm://localhost?broker.persistent=false");
        broker.start();


    }

    @BeforeEach
    public void beforeEach() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate build = restTemplateBuilder
                .basicAuthentication(USERNAME, PASSWORD)
                .build();

        this.restTemplate = build;
    }

    @Test
    void getQueues() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String url = "http://admin:admin@localhost:" + localServerPort + "/actuator/" +
                ActiveMqQueuesMonitorEndpoint.ENDPOINT_ID;
        System.out.println("URL: " + url);


        ParameterizedTypeReference t = ParameterizedTypeReference.forType(Collection.class);


        ResponseEntity<List<DestinationInfo>> exchange =
                restTemplate.exchange(url, HttpMethod.GET, entity,
                        new ParameterizedTypeReference<List<DestinationInfo>>() {
                        });

        Collection dstCollection = exchange.getBody();

        assertThat(dstCollection).hasSize(1);
        Iterator it = dstCollection.iterator();
        DestinationInfo next = (DestinationInfo) it.next();

        assertThat(next).isNotNull();
        assertThat(next.getQueueSize()).isEqualTo(0);


//        Thread.sleep(200000000);

    }
}