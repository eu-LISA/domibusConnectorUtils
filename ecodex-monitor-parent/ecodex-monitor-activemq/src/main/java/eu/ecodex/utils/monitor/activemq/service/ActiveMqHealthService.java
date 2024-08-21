/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.activemq.service;

import eu.ecodex.utils.monitor.activemq.config.ActiveMqHealthChecksConfigurationProperties;
import org.apache.activemq.broker.jmx.DestinationViewMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

public class ActiveMqHealthService extends AbstractHealthIndicator {

    @Autowired
    DestinationService destinationService;

    @Autowired
    ActiveMqHealthChecksConfigurationProperties config;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up();

        destinationService
                .getDestinations()
                .forEach(dst -> this.checkDestinationHealth(builder, dst));


    }

    private void checkDestinationHealth(Health.Builder builder, DestinationViewMBean dst) {
        String checkName = dst.getName() + "_usage";

        long queueSize = dst.getQueueSize();
        long maxPageSize = dst.getMaxPageSize();

        long usage = 0;
        if (queueSize > 0) {
            usage = maxPageSize / queueSize;
        }

        builder.withDetail(checkName + "_percentage", usage);
        builder.withDetail(checkName + "_size", queueSize);
        builder.withDetail(checkName + "_maxSize", maxPageSize);
        builder.withDetail(checkName + "_warn", config.getQueueSizeWarnThreshold());
        builder.withDetail(checkName + "_error", config.getQueueSizeErrorThreshold());

        if (usage < config.getQueueSizeWarnThreshold() &&
                usage < config.getQueueSizeErrorThreshold()) {
            builder.withDetail(checkName + "_state", "OK");
            return;
        }

        if (usage < config.getQueueSizeErrorThreshold()) {
            builder.withDetail(checkName + "_state", "WARN");
            return;
        }

        builder.down();
        builder.withDetail(checkName + "_state", "DOWN");

    }

}
