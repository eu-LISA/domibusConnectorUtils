/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.domibus.connector.test.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.Test;


public class MemoryAppenderTest {

    private static final Logger LOGGER = LogManager.getLogger(MemoryAppender.class);
    private static final Marker BUSINESS = MarkerManager.getMarker("BUSINESS");

    @Test
    public void testLogging() {
        LOGGER.info("Hello World!");
        LOGGER.info(BUSINESS, "Hello Business World!");
        LOGGER.info("Test");


        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender())
                .containsLogLine("Hello World!");

        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender()).filterOnMarker("BUSINESS")
                .hasLogLines(1);

    }


}