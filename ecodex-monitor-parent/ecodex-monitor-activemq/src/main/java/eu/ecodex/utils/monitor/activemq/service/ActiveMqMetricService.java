/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.activemq.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.activemq.broker.jmx.DestinationViewMBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ActiveMqMetricService {

    @Autowired
    DestinationService destinationService;

    @Autowired
    MeterRegistry meterRegistry;

//    @Autowired
//    BrokerFacade brokerFacade;


    List<DestinationViewMBean> destinations = new ArrayList<>();

    @PostConstruct
    public void init() throws Exception {
        destinationService
                .getDestinations()
                .stream()
                .forEach(this::addMetric);

    }

    private void addMetric(DestinationViewMBean dst) {

        //Stream<String> queueSize = Stream.of("queueSize", "maxPageSize");

        Gauge.Builder<DestinationViewMBean> builder =
                Gauge.builder("activemq.destinations." + dst.getName() + ".queueSize", dst,
                        DestinationViewMBean::getQueueSize);
        builder.description(
                "Number of messages on this destination, including any that have been dispatched but not acknowledged");
        builder.baseUnit("message");
        builder.register(meterRegistry);

        Gauge.Builder<DestinationViewMBean> builder1 =
                Gauge.builder("activemq.destinations." + dst.getName() + ".maxPageSize", dst,
                        DestinationViewMBean::getMaxPageSize);
        builder1.description("Maximum number of messages to be paged in");
        builder1.register(meterRegistry);


//        meterRegistry.gauge("activemq.destinations." + dst.getName(), dst, DestinationViewMBean::getQueueSize);

    }

}
