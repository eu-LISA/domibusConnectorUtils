/*
 * Copyright (c) 2024. European Union Agency for the Operational Management of Large-Scale IT Systems in the Area of Freedom, Security and Justice (eu-LISA)
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy at: https://joinup.ec.europa.eu/software/page/eupl
 */

package eu.ecodex.utils.configuration.ui.vaadin.tools.configfield;

import static org.assertj.core.api.Assertions.assertThat;

import eu.ecodex.utils.configuration.ui.vaadin.tools.UiConfigurationConversationService;
import java.time.Duration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;


@SpringBootTest(classes = DurationFieldFactoryTest.TestContext.class)
@Disabled("repair test on CI server")
public class DurationFieldFactoryTest {

    @Autowired
    @UiConfigurationConversationService
    ConversionService conversionService;

    @Test
    void testConversionServiceDuration() {
        String duration = "10s";

        Duration convert = conversionService.convert(duration, Duration.class);

        assertThat(convert).isEqualTo(Duration.ofSeconds(10));
    }

    @Test
    void testConversionServiceResource() {
        String duration = "classpath:/META-INF/spring.factories";

        Resource convert = conversionService.convert(duration, Resource.class);

//        assertThat(convert).isEqualTo(Duration.ofSeconds(10));
    }

    @SpringBootApplication(scanBasePackages = {"eu.ecodex.utils.configuration.ui.vaadin.tools",
            "eu.ecodex.configuration.spring"})
    public static class TestContext {

    }

}