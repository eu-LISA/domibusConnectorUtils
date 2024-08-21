/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.monitor.activemq.dto;

import lombok.Data;

@Data
public class DestinationInfo {

    private String name;

    private long queueSize;

    private DestinationType type;

    private long enqueueCount;

    private long dispatchCount;

    private long dequeueCount;

    private long storeMessageSize;

    private long memoryLimit;

    private long maxEnqueueTime;

    private long tempUsageLimit;

    private long maxPageSize;

    public enum DestinationType {
        QUEUE, TOPIC, NOT_KNOWN
    }

}
